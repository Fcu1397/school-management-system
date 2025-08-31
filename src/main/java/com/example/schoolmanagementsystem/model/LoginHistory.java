package com.example.schoolmanagementsystem.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * 登入歷史實體類別
 * 記錄使用者的登入活動
 */
@Entity
@Table(name = "LoginHistory")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "history_id")
    private Long historyId;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "login_time", nullable = false)
    private LocalDateTime loginTime;

    @Column(name = "logout_time")
    private LocalDateTime logoutTime;

    @Column(name = "ip_address", length = 45, nullable = false)
    private String ipAddress;

    @Column(name = "user_agent", length = 500)
    private String userAgent;

    @Column(name = "login_status", length = 20, nullable = false)
    private String loginStatus; // SUCCESS, FAILED, LOCKED

    @Column(name = "failure_reason", length = 255)
    private String failureReason; // 登入失敗原因

    @Column(name = "session_id", length = 100)
    private String sessionId;

    @Column(name = "device_type", length = 50)
    private String deviceType; // DESKTOP, MOBILE, TABLET

    @Column(name = "browser", length = 50)
    private String browser; // Chrome, Firefox, Safari, etc.

    @Column(name = "os", length = 50)
    private String os; // Windows, MacOS, Linux, iOS, Android

    @Column(name = "location", length = 255)
    private String location; // 地理位置（城市、國家）

    // 多個登入記錄對應一個使用者
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;
}