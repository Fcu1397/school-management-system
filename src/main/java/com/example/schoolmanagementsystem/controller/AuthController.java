package com.example.schoolmanagementsystem.controller;

import com.example.schoolmanagementsystem.dto.request.LoginRequest;
import com.example.schoolmanagementsystem.dto.request.UserRegistrationRequest;
import com.example.schoolmanagementsystem.dto.response.ApiResponse;
import com.example.schoolmanagementsystem.dto.response.UserResponse;
import com.example.schoolmanagementsystem.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

/**
 * 認證控制器
 * 處理使用者註冊、登入等認證相關的請求
 */
@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "認證管理", description = "使用者註冊、登入等認證相關操作")
public class AuthController {

    private final AuthService authService;

    /**
     * 使用者註冊
     */
    @PostMapping("/register")
    @Operation(summary = "註冊新使用者", description = "註冊學生、教師或管理員帳號")
    public ResponseEntity<ApiResponse<UserResponse>> register(
            @Valid @RequestBody UserRegistrationRequest request) {

        log.info("收到註冊請求: {}", request.getEmail());

        try {
            UserResponse user = authService.registerUser(request);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("註冊成功", user));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("REGISTRATION_FAILED", e.getMessage()));
        } catch (Exception e) {
            log.error("註冊失敗: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("SYSTEM_ERROR", "註冊失敗，請稍後再試"));
        }
    }

    /**
     * 使用者登入
     */
    @PostMapping("/login")
    @Operation(summary = "使用者登入", description = "使用 Email 和密碼登入系統")
    public ResponseEntity<ApiResponse<UserResponse>> login(
            @Valid @RequestBody LoginRequest request) {

        log.info("收到登入請求: {}", request.getEmail());

        try {
            UserResponse user = authService.login(request);
            return ResponseEntity.ok(ApiResponse.success("登入成功", user));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("LOGIN_FAILED", e.getMessage()));
        } catch (Exception e) {
            log.error("登入失敗: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("SYSTEM_ERROR", "登入失敗，請稍後再試"));
        }
    }

    /**
     * 檢查 Email 是否已存在
     */
    @GetMapping("/check-email")
    @Operation(summary = "檢查 Email", description = "檢查 Email 是否已被註冊")
    public ResponseEntity<ApiResponse<Map<String, Boolean>>> checkEmail(
            @RequestParam String email) {

        boolean exists = authService.isEmailExists(email);
        Map<String, Boolean> result = new HashMap<>();
        result.put("exists", exists);

        return ResponseEntity.ok(ApiResponse.success(result));
    }

    /**
     * 檢查學號是否已存在
     */
    @GetMapping("/check-student-id")
    @Operation(summary = "檢查學號", description = "檢查學號是否已被註冊")
    public ResponseEntity<ApiResponse<Map<String, Boolean>>> checkStudentId(
            @RequestParam String studentId) {

        boolean exists = authService.isStudentIdExists(studentId);
        Map<String, Boolean> result = new HashMap<>();
        result.put("exists", exists);

        return ResponseEntity.ok(ApiResponse.success(result));
    }

    /**
     * 檢查教職員編號是否已存在
     */
    @GetMapping("/check-teacher-id")
    @Operation(summary = "檢查教職員編號", description = "檢查教職員編號是否已被註冊")
    public ResponseEntity<ApiResponse<Map<String, Boolean>>> checkTeacherId(
            @RequestParam String teacherId) {

        boolean exists = authService.isTeacherIdExists(teacherId);
        Map<String, Boolean> result = new HashMap<>();
        result.put("exists", exists);

        return ResponseEntity.ok(ApiResponse.success(result));
    }

    /**
     * 登出（前端清除 Session 或 Token）
     */
    @PostMapping("/logout")
    @Operation(summary = "使用者登出", description = "登出系統")
    public ResponseEntity<ApiResponse<String>> logout() {
        // 實際的登出邏輯由前端處理（清除 token 或 session）
        // 這裡可以記錄登出日誌
        log.info("使用者登出");
        return ResponseEntity.ok(ApiResponse.success("登出成功", null));
    }
}