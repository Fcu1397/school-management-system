package com.example.schoolmanagementsystem.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 選課資訊回應 DTO
 * 用於回傳選課詳細資訊
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EnrollmentResponse {

    private Integer enrollmentId;
    private String studentId;
    private String studentName;
    private Integer classId;
    private String courseId;
    private String courseName;
    private BigDecimal credits;
    private String teacherName;
    private String semester;
    private Integer academicYear;
    private String schedule; // 上課時間
    private String location; // 上課地點
    private LocalDateTime enrollmentDate;

    // 成績資訊
    private BigDecimal score;
    private String grade; // A+, A, B+, etc.
    private Boolean isPassed;
}