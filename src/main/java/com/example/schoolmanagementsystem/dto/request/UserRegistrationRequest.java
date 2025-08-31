package com.example.schoolmanagementsystem.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 使用者註冊請求 DTO
 * 用於接收前端的註冊資料
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRegistrationRequest {

    @NotBlank(message = "Email 不可為空")
    @Email(message = "Email 格式不正確")
    private String email;

    @NotBlank(message = "密碼不可為空")
    @Size(min = 8, max = 20, message = "密碼長度必須在 8 到 20 個字元之間")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).*$",
            message = "密碼必須包含至少一個大寫字母、一個小寫字母和一個數字")
    private String password;

    @NotBlank(message = "確認密碼不可為空")
    private String confirmPassword;

    @NotBlank(message = "姓名不可為空")
    @Size(min = 2, max = 50, message = "姓名長度必須在 2 到 50 個字元之間")
    private String name;

    @NotBlank(message = "角色不可為空")
    @Pattern(regexp = "^(STUDENT|TEACHER|ADMIN)$",
            message = "角色必須是 STUDENT、TEACHER 或 ADMIN")
    private String roleName;

    // 學生專用欄位（當 roleName = STUDENT 時必填）
    @Pattern(regexp = "^[A-Z]\\d{8}$", message = "學號格式不正確（例：B10901234）")
    private String studentId;

    // 教師專用欄位（當 roleName = TEACHER 時必填）
    @Pattern(regexp = "^T\\d{6}$", message = "教職員編號格式不正確（例：T123456）")
    private String teacherId;

    // 部門資訊（選填）
    @Size(max = 100, message = "部門名稱不可超過 100 個字元")
    private String department;

    // 自定義驗證方法（可在 Service 層調用）
    public boolean isPasswordMatch() {
        return password != null && password.equals(confirmPassword);
    }
}