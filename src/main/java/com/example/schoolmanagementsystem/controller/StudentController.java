package com.example.schoolmanagementsystem.controller;

import com.example.schoolmanagementsystem.dto.request.EnrollmentRequest;
import com.example.schoolmanagementsystem.dto.response.ApiResponse;
import com.example.schoolmanagementsystem.dto.response.CourseResponse;
import com.example.schoolmanagementsystem.dto.response.EnrollmentResponse;
import com.example.schoolmanagementsystem.dto.response.StudentResponse;
import com.example.schoolmanagementsystem.model.Course;
import com.example.schoolmanagementsystem.repository.CourseRepository;
import com.example.schoolmanagementsystem.service.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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

    private final StudentService studentService;
    private final CourseRepository courseRepository;

    /**
     * 查詢學生資訊
     */
    @GetMapping("/{studentId}")
    @Operation(summary = "查詢學生資訊", description = "取得學生的詳細資訊和統計數據")
    public ResponseEntity<ApiResponse<StudentResponse>> getStudentInfo(
            @PathVariable String studentId) {

        log.info("查詢學生資訊: {}", studentId);

        try {
            StudentResponse studentInfo = studentService.getStudentInfo(studentId);
            return ResponseEntity.ok(ApiResponse.success("查詢成功", studentInfo));
        } catch (Exception e) {
            log.error("查詢學生資訊失敗: ", e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("STUDENT_NOT_FOUND", e.getMessage()));
        }
    }

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

        try {
            List<EnrollmentResponse> enrollments = studentService.getEnrollments(studentId);
            return ResponseEntity.ok(ApiResponse.success("查詢成功", enrollments));
        } catch (Exception e) {
            log.error("查詢選課記錄失敗: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("QUERY_FAILED", e.getMessage()));
        }
    }

    /**
     * 查詢特定學期選課
     */
    @GetMapping("/enrollments/{studentId}/semester")
    @Operation(summary = "查詢學期選課", description = "查詢特定學期的選課記錄")
    public ResponseEntity<ApiResponse<List<EnrollmentResponse>>> getEnrollmentsBySemester(
            @PathVariable String studentId,
            @RequestParam Integer academicYear,
            @RequestParam String semester) {

        log.info("查詢學生 {} 在 {} 年 {} 的選課記錄", studentId, academicYear, semester);

        try {
            List<EnrollmentResponse> enrollments =
                    studentService.getEnrollmentsBySemester(studentId, academicYear, semester);
            return ResponseEntity.ok(ApiResponse.success("查詢成功", enrollments));
        } catch (Exception e) {
            log.error("查詢學期選課失敗: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("QUERY_FAILED", e.getMessage()));
        }
    }

    /**
     * 選課
     */
    @PostMapping("/enroll")
    @Operation(summary = "選課", description = "學生選修課程")
    public ResponseEntity<ApiResponse<EnrollmentResponse>> enrollCourse(
            @Valid @RequestBody EnrollmentRequest request) {

        log.info("學生 {} 選修班級 {}", request.getStudentId(), request.getClassId());

        try {
            EnrollmentResponse enrollment = studentService.enrollInClass(request);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("選課成功", enrollment));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("ENROLLMENT_FAILED", e.getMessage()));
        } catch (Exception e) {
            log.error("選課失敗: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("SYSTEM_ERROR", "選課失敗，請稍後再試"));
        }
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

        try {
            boolean success = studentService.dropClass(studentId, classId);
            if (success) {
                return ResponseEntity.ok(ApiResponse.success("退選成功", "已成功退選課程"));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(ApiResponse.error("DROP_FAILED", "退選失敗"));
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("DROP_FAILED", e.getMessage()));
        } catch (Exception e) {
            log.error("退選失敗: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("SYSTEM_ERROR", "退選失敗，請稍後再試"));
        }
    }

    /**
     * 檢查衝堂
     */
    @GetMapping("/check-conflict")
    @Operation(summary = "檢查衝堂", description = "檢查選課是否有時間衝突")
    public ResponseEntity<ApiResponse<Boolean>> checkScheduleConflict(
            @RequestParam String studentId,
            @RequestParam Integer classId) {

        log.info("檢查學生 {} 選修班級 {} 是否衝堂", studentId, classId);

        try {
            boolean hasConflict = studentService.hasScheduleConflict(studentId, classId);
            return ResponseEntity.ok(ApiResponse.success(
                    hasConflict ? "有時間衝突" : "無時間衝突",
                    hasConflict
            ));
        } catch (Exception e) {
            log.error("檢查衝堂失敗: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("CHECK_FAILED", e.getMessage()));
        }
    }
}