package com.example.schoolmanagementsystem.service.impl;

import com.example.schoolmanagementsystem.dto.request.EnrollmentRequest;
import com.example.schoolmanagementsystem.dto.response.EnrollmentResponse;
import com.example.schoolmanagementsystem.dto.response.StudentResponse;
import com.example.schoolmanagementsystem.exception.DuplicateResourceException;
import com.example.schoolmanagementsystem.exception.ResourceNotFoundException;
import com.example.schoolmanagementsystem.model.*;
import com.example.schoolmanagementsystem.repository.*;
import com.example.schoolmanagementsystem.service.StudentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 學生服務實作類別
 * 實作學生相關的業務邏輯
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final ClassInfoRepository classInfoRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final GradeRepository gradeRepository;
    private final AuditLogRepository auditLogRepository;

    @Override
    public StudentResponse getStudentInfo(String studentId) {
        log.info("查詢學生資訊: {}", studentId);

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("學生", "學號", studentId));

        // 計算統計資訊
        List<Enrollment> enrollments = enrollmentRepository.findByStudent_StudentId(studentId);
        int currentCourses = enrollments.size();

        // 計算已修學分和GPA
        double totalCredits = 0;
        double totalGradePoints = 0;
        int completedCourses = 0;

        for (Enrollment enrollment : enrollments) {
            Grade grade = gradeRepository.findByEnrollment(enrollment).orElse(null);
            if (grade != null && grade.getScoreNumeric() != null) {
                completedCourses++;
                double credits = enrollment.getClassInfo().getCourse().getCredits().doubleValue();
                totalCredits += credits;
                totalGradePoints += grade.getScoreNumeric().doubleValue() * credits;
            }
        }

        double gpa = totalCredits > 0 ? totalGradePoints / totalCredits : 0.0;

        return StudentResponse.builder()
                .studentId(student.getStudentId())
                .studentName(student.getStudentName())
                .email(student.getUser().getEmail())
                .currentCourses(currentCourses)
                .completedCourses(completedCourses)
                .totalCredits((int) totalCredits)
                .gpa(Math.round(gpa * 100.0) / 100.0)  // 四捨五入到小數點後兩位
                .build();
    }

    @Override
    @Transactional
    public EnrollmentResponse enrollInClass(EnrollmentRequest request) {
        log.info("處理選課請求: 學生 {} 選修班級 {}", request.getStudentId(), request.getClassId());

        // 1. 檢查學生是否存在
        Student student = studentRepository.findById(request.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("學生", "學號", request.getStudentId()));

        // 2. 檢查班級是否存在
        ClassInfo classInfo = classInfoRepository.findById(request.getClassId())
                .orElseThrow(() -> new ResourceNotFoundException("班級", "班級ID", request.getClassId()));

        // 3. 檢查是否已選修
        boolean alreadyEnrolled = enrollmentRepository
                .existsByStudent_StudentIdAndClassInfo_ClassId(request.getStudentId(), request.getClassId());

        if (alreadyEnrolled) {
            throw new DuplicateResourceException("選課記錄", "學生已選修此課程", request.getClassId());
        }

        // 4. 檢查班級人數是否已滿
        Long currentEnrollment = enrollmentRepository.countByClassId(request.getClassId());
        if (currentEnrollment >= classInfo.getCapacity()) {
            throw new IllegalArgumentException(
                    String.format("班級已滿（%d/%d）", currentEnrollment, classInfo.getCapacity())
            );
        }

        // 5. 檢查是否有衝堂
        if (hasScheduleConflict(request.getStudentId(), request.getClassId())) {
            throw new IllegalArgumentException("選課失敗：課程時間衝突");
        }

        // 6. 檢查學分上限（假設每學期最多 25 學分）
        double currentCredits = calculateCurrentSemesterCredits(
                request.getStudentId(),
                classInfo.getAcademicYear(),
                classInfo.getSemester()
        );
        double newCredits = classInfo.getCourse().getCredits().doubleValue();

        if (currentCredits + newCredits > 25) {
            throw new IllegalArgumentException(
                    String.format("超過學分上限（目前：%.1f，新增：%.1f，上限：25）",
                            currentCredits, newCredits)
            );
        }

        // 7. 建立選課記錄
        Enrollment enrollment = Enrollment.builder()
                .student(student)
                .classInfo(classInfo)
                .build();

        enrollment = enrollmentRepository.save(enrollment);

        // 8. 記錄稽核日誌
        recordAuditLog("ENROLL", "Enrollment", enrollment.getEnrollmentId().toString(),
                student.getUser().getUserId(),
                String.format("學生 %s 選修 %s", student.getStudentId(), classInfo.getCourse().getCourseName()));

        log.info("選課成功: 學生 {} 選修 {}", request.getStudentId(), classInfo.getCourse().getCourseName());

        // 9. 建立回應
        return buildEnrollmentResponse(enrollment);
    }

    @Override
    @Transactional
    public boolean dropClass(String studentId, Integer classId) {
        log.info("處理退選請求: 學生 {} 退選班級 {}", studentId, classId);

        // 1. 查詢選課記錄
        Enrollment enrollment = enrollmentRepository
                .findByStudentAndClassInfo(
                        studentRepository.findById(studentId)
                                .orElseThrow(() -> new ResourceNotFoundException("學生", "學號", studentId)),
                        classInfoRepository.findById(classId)
                                .orElseThrow(() -> new ResourceNotFoundException("班級", "班級ID", classId))
                )
                .orElseThrow(() -> new ResourceNotFoundException("選課記錄", "學生未選修此課程", classId));

        // 2. 檢查是否已有成績（有成績就不能退選）
        Grade grade = gradeRepository.findByEnrollment(enrollment).orElse(null);
        if (grade != null && grade.getScoreNumeric() != null) {
            throw new IllegalArgumentException("已有成績的課程無法退選");
        }

        // 3. 檢查退選期限（這裡假設開學後兩週內可退選）
        // TODO: 實作退選期限檢查

        // 4. 刪除選課記錄
        enrollmentRepository.delete(enrollment);

        // 5. 記錄稽核日誌
        Student student = studentRepository.findById(studentId).orElse(null);
        if (student != null) {
            recordAuditLog("DROP", "Enrollment", enrollment.getEnrollmentId().toString(),
                    student.getUser().getUserId(),
                    String.format("學生 %s 退選 %s", studentId, enrollment.getClassInfo().getCourse().getCourseName()));
        }

        log.info("退選成功: 學生 {} 退選班級 {}", studentId, classId);

        return true;
    }

    @Override
    public List<EnrollmentResponse> getEnrollments(String studentId) {
        log.info("查詢學生 {} 的所有選課記錄", studentId);

        List<Enrollment> enrollments = enrollmentRepository.findByStudent_StudentId(studentId);

        return enrollments.stream()
                .map(this::buildEnrollmentResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<EnrollmentResponse> getEnrollmentsBySemester(String studentId, Integer academicYear, String semester) {
        log.info("查詢學生 {} 在 {} 年 {} 的選課記錄", studentId, academicYear, semester);

        List<Enrollment> enrollments = enrollmentRepository.findByStudent_StudentId(studentId);

        // 過濾特定學期的選課
        List<Enrollment> semesterEnrollments = enrollments.stream()
                .filter(e -> e.getClassInfo().getAcademicYear().equals(academicYear))
                .filter(e -> e.getClassInfo().getSemester().equals(semester))
                .collect(Collectors.toList());

        return semesterEnrollments.stream()
                .map(this::buildEnrollmentResponse)
                .collect(Collectors.toList());
    }

    @Override
    public boolean hasScheduleConflict(String studentId, Integer classId) {
        log.debug("檢查學生 {} 選修班級 {} 是否有衝堂", studentId, classId);

        // 取得要選的班級資訊
        ClassInfo newClass = classInfoRepository.findById(classId)
                .orElseThrow(() -> new ResourceNotFoundException("班級", "班級ID", classId));

        // 取得學生當學期已選的課程
        List<Enrollment> currentEnrollments = enrollmentRepository.findByStudent_StudentId(studentId);

        // 過濾同一學期的課程
        List<ClassInfo> sameTermClasses = currentEnrollments.stream()
                .map(Enrollment::getClassInfo)
                .filter(c -> c.getAcademicYear().equals(newClass.getAcademicYear()))
                .filter(c -> c.getSemester().equals(newClass.getSemester()))
                .collect(Collectors.toList());

        // TODO: 實作時間衝突檢查邏輯
        // 這裡需要解析 schedule_time 欄位來判斷是否衝突
        // 暫時回傳 false（不衝突）

        return false;
    }

    /**
     * 計算當前學期已選學分數
     */
    private double calculateCurrentSemesterCredits(String studentId, Integer academicYear, String semester) {
        List<Enrollment> enrollments = enrollmentRepository.findByStudent_StudentId(studentId);

        return enrollments.stream()
                .filter(e -> e.getClassInfo().getAcademicYear().equals(academicYear))
                .filter(e -> e.getClassInfo().getSemester().equals(semester))
                .mapToDouble(e -> e.getClassInfo().getCourse().getCredits().doubleValue())
                .sum();
    }

    /**
     * 建立選課回應 DTO
     */
    private EnrollmentResponse buildEnrollmentResponse(Enrollment enrollment) {
        // 查詢成績
        Grade grade = gradeRepository.findByEnrollment(enrollment).orElse(null);

        return EnrollmentResponse.builder()
                .enrollmentId(enrollment.getEnrollmentId())
                .studentId(enrollment.getStudent().getStudentId())
                .studentName(enrollment.getStudent().getStudentName())
                .classId(enrollment.getClassInfo().getClassId())
                .courseId(enrollment.getClassInfo().getCourse().getCourseId())
                .courseName(enrollment.getClassInfo().getCourse().getCourseName())
                .credits(enrollment.getClassInfo().getCourse().getCredits())
                .teacherName(enrollment.getClassInfo().getTeacher().getTeacherName())
                .semester(enrollment.getClassInfo().getSemester())
                .academicYear(enrollment.getClassInfo().getAcademicYear())
                .score(grade != null ? grade.getScoreNumeric() : null)
                .isPassed(grade != null && grade.getScoreNumeric() != null
                        && grade.getScoreNumeric().doubleValue() >= 60.0)
                .build();
    }

    /**
     * 記錄稽核日誌
     */
    private void recordAuditLog(String action, String entityType, String entityId,
                                Integer userId, String description) {
        try {
            AuditLog log = AuditLog.builder()
                    .action(action)
                    .entityType(entityType)
                    .entityId(entityId)
                    .userId(userId)
                    .userEmail("system")  // TODO: 從 SecurityContext 取得
                    .description(description)
                    .ipAddress("127.0.0.1")  // TODO: 從 Request 取得
                    .timestamp(LocalDateTime.now())
                    .success(true)
                    .build();

            auditLogRepository.save(log);
        } catch (Exception e) {
            log.error("記錄稽核日誌失敗: ", e);
        }
    }
}