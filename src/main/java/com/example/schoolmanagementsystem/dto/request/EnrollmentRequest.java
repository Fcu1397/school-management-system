package com.example.schoolmanagementsystem.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 選課請求 DTO
 * 用於學生選課操作
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EnrollmentRequest {

    @NotBlank(message = "學生ID不可為空")
    private String studentId;

    @NotNull(message = "班級ID不可為空")
    @Positive(message = "班級ID必須為正數")
    private Integer classId;

    // 選課原因（選填）
    private String reason;
}