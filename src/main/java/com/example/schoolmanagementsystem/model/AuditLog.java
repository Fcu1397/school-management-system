package com.example.schoolmanagementsystem.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * 稽核日誌實體類別
 * 記錄系統中的重要操作紀錄
 */
@Entity
@Table(name = "AuditLogs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "log_id")
    private Long logId;

    @Column(name = "action", length = 50, nullable = false)
    private String action; // CREATE, UPDATE, DELETE, LOGIN, LOGOUT

    @Column(name = "entity_type", length = 50)
    private String entityType; // Student, Course, Grade, etc.

    @Column(name = "entity_id", length = 50)
    private String entityId; // 被操作的實體ID

    @Column(name = "user_id", nullable = false)
    private Integer userId; // 執行操作的使用者ID

    @Column(name = "user_email", nullable = false)
    private String userEmail; // 執行操作的使用者Email

    @Column(name = "description", columnDefinition = "TEXT")
    private String description; // 操作詳細描述

    @Column(name = "old_value", columnDefinition = "TEXT")
    private String oldValue; // 修改前的值（JSON格式）

    @Column(name = "new_value", columnDefinition = "TEXT")
    private String newValue; // 修改後的值（JSON格式）

    @Column(name = "ip_address", length = 45)
    private String ipAddress; // IPv4 或 IPv6

    @Column(name = "user_agent", length = 500)
    private String userAgent; // 瀏覽器資訊

    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;

    @Column(name = "success", nullable = false)
    private Boolean success; // 操作是否成功

    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage; // 錯誤訊息（如果失敗）
}