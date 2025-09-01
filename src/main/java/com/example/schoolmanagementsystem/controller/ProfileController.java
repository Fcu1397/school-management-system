package com.example.schoolmanagementsystem.controller;

import com.example.schoolmanagementsystem.dto.request.ProfileUpdateRequest;
import com.example.schoolmanagementsystem.dto.response.ApiResponse;
import com.example.schoolmanagementsystem.dto.response.ProfileResponse;
import com.example.schoolmanagementsystem.service.ProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 個人資料控制器
 * 處理使用者個人資料相關的 API 請求
 */
@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
@Tag(name = "個人資料管理", description = "使用者個人資料相關 API")
public class ProfileController {

    private final ProfileService profileService;

    /**
     * 獲取目前使用者的個人資料
     * @param userId 使用者ID（暫時從路徑參數獲取，實際專案中應從認證資訊獲取）
     * @return 個人資料資訊
     */
    @GetMapping("/{userId}")
    @Operation(summary = "獲取個人資料", description = "獲取指定使用者的個人資料")
    public ResponseEntity<ApiResponse<ProfileResponse>> getUserProfile(@PathVariable Integer userId) {
        ProfileResponse profile = profileService.getUserProfile(userId);
        return ResponseEntity.ok(ApiResponse.success("獲取個人資料成功", profile));
    }

    /**
     * 更新目前使用者的個人資料
     * @param userId 使用者ID
     * @param request 個人資料更新請求
     * @return 更新後的個人資料
     */
    @PutMapping("/{userId}")
    @Operation(summary = "更新個人資料", description = "更新指定使用者的個人資料")
    public ResponseEntity<ApiResponse<ProfileResponse>> updateUserProfile(
            @PathVariable Integer userId,
            @Valid @RequestBody ProfileUpdateRequest request) {

        ProfileResponse updatedProfile = profileService.updateUserProfile(userId, request);
        return ResponseEntity.ok(ApiResponse.success("個人資料更新成功", updatedProfile));
    }

    /**
     * 獲取目前使用者的個人資料（無需指定ID，從認證資訊獲取）
     * 注意：這個方法在實際專案中需要整合 Spring Security 來獲取當前認證使用者
     * @return 個人資料資訊
     */
    @GetMapping("/me")
    @Operation(summary = "獲取我的個人資料", description = "獲取當前登入使用者的個人資料")
    public ResponseEntity<ApiResponse<String>> getMyProfile() {
        // TODO: 實作 Spring Security 整合後，從 SecurityContext 獲取當前使用者ID
        return ResponseEntity.ok(ApiResponse.success("請使用 /api/profile/{userId} 來獲取個人資料",
                "此功能需要整合認證系統"));
    }

    /**
     * 更新目前使用者的個人資料（無需指定ID，從認證資訊獲取）
     * @param request 個人資料更新請求
     * @return 更新後的個人資料
     */
    @PutMapping("/me")
    @Operation(summary = "更新我的個人資料", description = "更新當前登入使用者的個人資料")
    public ResponseEntity<ApiResponse<String>> updateMyProfile(@Valid @RequestBody ProfileUpdateRequest request) {
        // TODO: 實作 Spring Security 整合後，從 SecurityContext 獲取當前使用者ID
        return ResponseEntity.ok(ApiResponse.success("請使用 /api/profile/{userId} 來更新個人資料",
                "此功能需要整合認證系統"));
    }
}
