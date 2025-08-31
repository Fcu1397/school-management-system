package com.example.schoolmanagementsystem.controller;

import com.example.schoolmanagementsystem.dto.response.ApiResponse;
import com.example.schoolmanagementsystem.model.ClassInfo;
import com.example.schoolmanagementsystem.model.Course;
import com.example.schoolmanagementsystem.repository.ClassInfoRepository;
import com.example.schoolmanagementsystem.repository.CourseRepository;
import com.example.schoolmanagementsystem.repository.EnrollmentRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 課程管理控制器
 * 處理課程和班級相關的查詢
 */
@Slf4j
@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
@Tag(name = "課程管理", description = "課程和班級相關操作")
public class CourseController {

    private final CourseRepository courseRepository;
    private final ClassInfoRepository classInfoRepository;
    private final EnrollmentRepository enrollmentRepository;

    /**
     * 查詢所有課程
     */
    @GetMapping
    @Operation(summary = "查詢所有課程", description = "取得所有課程清單")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getAllCourses() {
        log.info("查詢所有課程");

        List<Course> courses = courseRepository.findAll();
        List<Map<String, Object>> courseList = courses.stream()
                .map(course -> {
                    Map<String, Object> courseMap = new HashMap<>();
                    courseMap.put("courseId", course.getCourseId());
                    courseMap.put("courseName", course.getCourseName());
                    courseMap.put("credits", course.getCredits());

                    // 查詢該課程的開課班級數
                    List<ClassInfo> classes = classInfoRepository.findByCourse(course);
                    courseMap.put("totalClasses", classes.size());

                    return courseMap;
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(ApiResponse.success("查詢成功", courseList));
    }

    /**
     * 查詢特定課程的所有班級
     */
    @GetMapping("/{courseId}/classes")
    @Operation(summary = "查詢課程班級", description = "查詢特定課程的所有開課班級")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getClassesByCourse(
            @PathVariable String courseId) {

        log.info("查詢課程 {} 的所有班級", courseId);

        List<ClassInfo> classes = classInfoRepository.findByCourse_CourseId(courseId);
        List<Map<String, Object>> classList = classes.stream()
                .map(classInfo -> {
                    Map<String, Object> classMap = new HashMap<>();
                    classMap.put("classId", classInfo.getClassId());
                    classMap.put("courseId", classInfo.getCourse().getCourseId());
                    classMap.put("courseName", classInfo.getCourse().getCourseName());
                    classMap.put("teacherName", classInfo.getTeacher().getTeacherName());
                    classMap.put("academicYear", classInfo.getAcademicYear());
                    classMap.put("semester", classInfo.getSemester());
                    classMap.put("capacity", classInfo.getCapacity());

                    // 查詢目前選課人數
                    Long currentEnrollment = enrollmentRepository.countByClassId(classInfo.getClassId());
                    classMap.put("currentEnrollment", currentEnrollment);
                    classMap.put("available", classInfo.getCapacity() - currentEnrollment);

                    return classMap;
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(ApiResponse.success("查詢成功", classList));
    }

    /**
     * 查詢可選班級（當前學期）
     */
    @GetMapping("/available")
    @Operation(summary = "查詢可選班級", description = "查詢當前學期所有可選的班級")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getAvailableClasses(
            @RequestParam(defaultValue = "2024") Integer academicYear,
            @RequestParam(defaultValue = "上學期") String semester) {

        log.info("查詢 {} 年 {} 的可選班級", academicYear, semester);

        List<ClassInfo> classes = classInfoRepository.findByAcademicYearAndSemester(academicYear, semester);
        List<Map<String, Object>> availableClasses = classes.stream()
                .map(classInfo -> {
                    Map<String, Object> classMap = new HashMap<>();
                    classMap.put("classId", classInfo.getClassId());
                    classMap.put("courseId", classInfo.getCourse().getCourseId());
                    classMap.put("courseName", classInfo.getCourse().getCourseName());
                    classMap.put("credits", classInfo.getCourse().getCredits());
                    classMap.put("teacherId", classInfo.getTeacher().getTeacherId());
                    classMap.put("teacherName", classInfo.getTeacher().getTeacherName());
                    classMap.put("capacity", classInfo.getCapacity());

                    // 查詢目前選課人數
                    Long currentEnrollment = enrollmentRepository.countByClassId(classInfo.getClassId());
                    classMap.put("currentEnrollment", currentEnrollment);
                    classMap.put("remainingSeats", classInfo.getCapacity() - currentEnrollment);
                    classMap.put("isFull", currentEnrollment >= classInfo.getCapacity());

                    return classMap;
                })
                .filter(classMap -> !(Boolean) classMap.get("isFull"))  // 只顯示未滿的班級
                .collect(Collectors.toList());

        return ResponseEntity.ok(ApiResponse.success("查詢成功", availableClasses));
    }

    /**
     * 查詢班級詳細資訊
     */
    @GetMapping("/class/{classId}")
    @Operation(summary = "查詢班級詳情", description = "查詢特定班級的詳細資訊")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getClassDetails(
            @PathVariable Integer classId) {

        log.info("查詢班級 {} 的詳細資訊", classId);

        ClassInfo classInfo = classInfoRepository.findById(classId)
                .orElse(null);

        if (classInfo == null) {
            return ResponseEntity.notFound().build();
        }

        Map<String, Object> details = new HashMap<>();
        details.put("classId", classInfo.getClassId());
        details.put("courseId", classInfo.getCourse().getCourseId());
        details.put("courseName", classInfo.getCourse().getCourseName());
        details.put("credits", classInfo.getCourse().getCredits());
        details.put("teacherId", classInfo.getTeacher().getTeacherId());
        details.put("teacherName", classInfo.getTeacher().getTeacherName());
        details.put("academicYear", classInfo.getAcademicYear());
        details.put("semester", classInfo.getSemester());
        details.put("capacity", classInfo.getCapacity());

        // 查詢選課統計
        Long currentEnrollment = enrollmentRepository.countByClassId(classId);
        details.put("currentEnrollment", currentEnrollment);
        details.put("remainingSeats", classInfo.getCapacity() - currentEnrollment);
        details.put("enrollmentRate", (currentEnrollment * 100.0) / classInfo.getCapacity());

        return ResponseEntity.ok(ApiResponse.success("查詢成功", details));
    }
}