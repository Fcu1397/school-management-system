package com.example.schoolmanagementsystem.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * 使用者資訊回應 DTO
 * 用於回傳使用者資訊（不含敏感資料）
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {

    private Integer userId;
    private String email;
    private String roleName;
    private String name;
    private String identifier; // 學號或教職員編號
    private String department;
    private LocalDateTime createdAt;
    private LocalDateTime lastLoginTime;

    // 使用者狀態
    private boolean isActive;
    private boolean isLocked;
}