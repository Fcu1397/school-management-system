package com.example.schoolmanagementsystem.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * 統一的 API 回應格式
 * @param <T> 回應資料的類型
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiResponse<T> {

    // 是否成功
    private boolean success;

    // 回應訊息
    private String message;

    // 回應資料
    private T data;

    // 錯誤代碼（當 success = false 時使用）
    private String errorCode;

    // 時間戳記
    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();

    // 靜態工廠方法：成功回應
    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.<T>builder()
                .success(true)
                .message("操作成功")
                .data(data)
                .build();
    }

    // 靜態工廠方法：成功回應（含自定義訊息）
    public static <T> ApiResponse<T> success(String message, T data) {
        return ApiResponse.<T>builder()
                .success(true)
                .message(message)
                .data(data)
                .build();
    }

    // 靜態工廠方法：失敗回應
    public static <T> ApiResponse<T> error(String message) {
        return ApiResponse.<T>builder()
                .success(false)
                .message(message)
                .build();
    }

    // 靜態工廠方法：失敗回應（含錯誤代碼）
    public static <T> ApiResponse<T> error(String errorCode, String message) {
        return ApiResponse.<T>builder()
                .success(false)
                .errorCode(errorCode)
                .message(message)
                .build();
    }
}