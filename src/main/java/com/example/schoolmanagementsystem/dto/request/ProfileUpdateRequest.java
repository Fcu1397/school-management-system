package com.example.schoolmanagementsystem.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * 個人資料更新請求 DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfileUpdateRequest {

    @Email(message = "請輸入有效的 email 格式")
    private String email;

    private String fullName;

    @Pattern(regexp = "^[0-9-+()\\s]*$", message = "電話號碼格式不正確")
    private String phone;

    private String address;

    private LocalDate birthDate;

    @Pattern(regexp = "^(男|女|其他)$", message = "性別只能是：男、女、其他")
    private String gender;

    private String profilePicture;
}
