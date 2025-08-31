package com.example.schoolmanagementsystem.repository;

import com.example.schoolmanagementsystem.model.LoginHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 登入歷史資料存取介面
 * 提供登入記錄相關的資料庫操作
 */
@Repository
public interface LoginHistoryRepository extends JpaRepository<LoginHistory, Long> {

    /**
     * 查詢使用者的登入歷史
     * @param userId 使用者ID
     * @param pageable 分頁參數
     * @return 登入歷史分頁
     */
    Page<LoginHistory> findByUserIdOrderByLoginTimeDesc(Integer userId, Pageable pageable);

    /**
     * 查詢最近的登入記錄
     * @param userId 使用者ID
     * @return 最近的登入記錄
     */
    Optional<LoginHistory> findTopByUserIdOrderByLoginTimeDesc(Integer userId);

    /**
     * 查詢時間範圍內的登入記錄
     * @param startTime 開始時間
     * @param endTime 結束時間
     * @return 登入記錄列表
     */
    List<LoginHistory> findByLoginTimeBetween(LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 查詢失敗的登入嘗試
     * @param email Email
     * @param status 登入狀態
     * @param since 起始時間
     * @return 失敗記錄列表
     */
    List<LoginHistory> findByEmailAndLoginStatusAndLoginTimeAfter(
            String email, String status, LocalDateTime since);

    /**
     * 統計登入失敗次數
     * @param email Email
     * @param since 起始時間
     * @return 失敗次數
     */
    @Query("SELECT COUNT(l) FROM LoginHistory l WHERE l.email = :email " +
            "AND l.loginStatus = 'FAILED' AND l.loginTime > :since")
    Long countFailedAttempts(@Param("email") String email, @Param("since") LocalDateTime since);

    /**
     * 查詢活躍的會話
     * @param sessionId 會話ID
     * @return 登入記錄
     */
    Optional<LoginHistory> findBySessionIdAndLogoutTimeIsNull(String sessionId);

    /**
     * 根據IP查詢登入記錄
     * @param ipAddress IP地址
     * @param pageable 分頁參數
     * @return 登入記錄分頁
     */
    Page<LoginHistory> findByIpAddress(String ipAddress, Pageable pageable);
}