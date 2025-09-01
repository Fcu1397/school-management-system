package com.example.schoolmanagementsystem.service;

import com.example.schoolmanagementsystem.dto.request.ProfileUpdateRequest;
import com.example.schoolmanagementsystem.dto.response.ProfileResponse;

/**
 * 個人資料服務介面
 */
public interface ProfileService {

    /**
     * 獲取使用者個人資料
     * @param userId 使用者ID
     * @return 個人資料回應
     */
    ProfileResponse getUserProfile(Integer userId);

    /**
     * 更新使用者個人資料
     * @param userId 使用者ID
     * @param request 更新請求
     * @return 更新後的個人資料
     */
    ProfileResponse updateUserProfile(Integer userId, ProfileUpdateRequest request);
}
