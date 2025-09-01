package com.example.schoolmanagementsystem.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 個人資料回應 DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfileResponse {

    private Integer userId;
    private String email;
    private String fullName;
    private String phone;
    private String address;
    private LocalDate birthDate;
    private String gender;
    private String profilePicture;
    private String roleName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
