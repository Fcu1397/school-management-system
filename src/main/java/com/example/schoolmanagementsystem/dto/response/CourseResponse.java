package com.example.schoolmanagementsystem.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

/**
 * 課程資訊回應 DTO
 * 用於回傳課程詳細資訊
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseResponse {

    private String courseId;
    private String courseName;
    private BigDecimal credits;
    private String description;
    private String prerequisiteCourseId;
    private String prerequisiteCourseName;

    // 開課資訊
    private Integer totalClasses; // 總開課班級數
    private Integer availableClasses; // 可選班級數
}