package com.example.schoolmanagementsystem.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 學生資訊回應 DTO
 * 用於回傳學生詳細資訊
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentResponse {

    private String studentId;
    private String studentName;
    private String email;
    private String department;
    private Integer enrollmentYear;
    private String status; // 在學、休學、畢業
    private Integer totalCredits; // 已修學分
    private Double gpa; // 平均成績

    // 統計資訊
    private Integer currentCourses; // 本學期選課數
    private Integer completedCourses; // 已完成課程數
}