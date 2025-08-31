package com.example.schoolmanagementsystem.service;

import com.example.schoolmanagementsystem.dto.request.LoginRequest;
import com.example.schoolmanagementsystem.dto.request.UserRegistrationRequest;
import com.example.schoolmanagementsystem.dto.response.UserResponse;

/**
 * 認證服務介面
 * 定義使用者認證相關的業務邏輯
 */
public interface AuthService {

    /**
     * 註冊新使用者
     * @param request 註冊請求資料
     * @return 註冊成功的使用者資訊
     */
    UserResponse registerUser(UserRegistrationRequest request);

    /**
     * 使用者登入
     * @param request 登入請求資料
     * @return 登入成功的使用者資訊
     */
    UserResponse login(LoginRequest request);

    /**
     * 驗證 Email 是否已存在
     * @param email 電子郵件
     * @return 是否存在
     */
    boolean isEmailExists(String email);

    /**
     * 驗證學號是否已存在
     * @param studentId 學號
     * @return 是否存在
     */
    boolean isStudentIdExists(String studentId);

    /**
     * 驗證教職員編號是否已存在
     * @param teacherId 教職員編號
     * @return 是否存在
     */
    boolean isTeacherIdExists(String teacherId);
}