package com.example.schoolmanagementsystem.controller;

import com.example.schoolmanagementsystem.dto.response.ApiResponse;
import com.example.schoolmanagementsystem.dto.response.CourseResponse;
import com.example.schoolmanagementsystem.dto.response.EnrollmentResponse;
import com.example.schoolmanagementsystem.model.Course;
import com.example.schoolmanagementsystem.model.Enrollment;
import com.example.schoolmanagementsystem.repository.CourseRepository;
import com.example.schoolmanagementsystem.repository.EnrollmentRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 學生功能控制器
 * 處理學生相關的操作（選課、查詢成績等）
 */
@Slf4j
@RestController
@RequestMapping("/api/student")
@RequiredArgsConstructor
@Tag(name = "學生功能", description = "學生選課、查詢等相關操作")
public class StudentController {

    private final EnrollmentRepository enrollmentRepository;
    private final CourseRepository courseRepository;

    /**
     * 查詢可選課程
     */
    @GetMapping("/courses/available")
    @Operation(summary = "查詢可選課程", description = "取得所有可選的課程清單")
    public ResponseEntity<ApiResponse<List<CourseResponse>>> getAvailableCourses() {
        log.info("查詢可選課程");

        List<Course> courses = courseRepository.findAll();
        List<CourseResponse> courseResponses = courses.stream()
                .map(course -> CourseResponse.builder()
                        .courseId(course.getCourseId())
                        .courseName(course.getCourseName())
                        .credits(course.getCredits())
                        .build())
                .collect(Collectors.toList());

        return ResponseEntity.ok(ApiResponse.success("查詢成功", courseResponses));
    }

    /**
     * 查詢我的選課
     */
    @GetMapping("/enrollments/{studentId}")
    @Operation(summary = "查詢選課清單", description = "查詢特定學生的選課記錄")
    public ResponseEntity<ApiResponse<List<EnrollmentResponse>>> getMyEnrollments(
            @PathVariable String studentId) {

        log.info("查詢學生 {} 的選課記錄", studentId);

        List<Enrollment> enrollments = enrollmentRepository.findByStudent_StudentId(studentId);
        List<EnrollmentResponse> responses = enrollments.stream()
                .map(enrollment -> EnrollmentResponse.builder()
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
                        .build())
                .collect(Collectors.toList());

        return ResponseEntity.ok(ApiResponse.success("查詢成功", responses));
    }

    /**
     * 選課
     */
    @PostMapping("/enroll")
    @Operation(summary = "選課", description = "學生選修課程")
    public ResponseEntity<ApiResponse<String>> enrollCourse(
            @RequestParam String studentId,
            @RequestParam Integer classId) {

        log.info("學生 {} 選修班級 {}", studentId, classId);

        // 檢查是否已選修
        boolean exists = enrollmentRepository.existsByStudent_StudentIdAndClassInfo_ClassId(studentId, classId);
        if (exists) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("ALREADY_ENROLLED", "您已選修此課程"));
        }

        // TODO: 實作選課邏輯（檢查人數上限、衝堂等）

        return ResponseEntity.ok(ApiResponse.success("選課成功", "已成功選修課程"));
    }

    /**
     * 退選
     */
    @DeleteMapping("/drop")
    @Operation(summary = "退選", description = "學生退選課程")
    public ResponseEntity<ApiResponse<String>> dropCourse(
            @RequestParam String studentId,
            @RequestParam Integer classId) {

        log.info("學生 {} 退選班級 {}", studentId, classId);

        // TODO: 實作退選邏輯

        return ResponseEntity.ok(ApiResponse.success("退選成功", "已成功退選課程"));
    }
}