package com.example.schoolmanagementsystem.repository;

import com.example.schoolmanagementsystem.model.AuditLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 稽核日誌資料存取介面
 * 提供系統操作紀錄的資料庫操作
 */
@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {

    /**
     * 根據使用者ID查詢操作紀錄
     * @param userId 使用者ID
     * @param pageable 分頁參數
     * @return 操作紀錄分頁
     */
    Page<AuditLog> findByUserId(Integer userId, Pageable pageable);

    /**
     * 根據操作類型查詢
     * @param action 操作類型
     * @param pageable 分頁參數
     * @return 操作紀錄分頁
     */
    Page<AuditLog> findByAction(String action, Pageable pageable);

    /**
     * 查詢特定實體的操作紀錄
     * @param entityType 實體類型
     * @param entityId 實體ID
     * @return 操作紀錄列表
     */
    List<AuditLog> findByEntityTypeAndEntityIdOrderByTimestampDesc(
            String entityType, String entityId);

    /**
     * 查詢時間範圍內的操作紀錄
     * @param startTime 開始時間
     * @param endTime 結束時間
     * @param pageable 分頁參數
     * @return 操作紀錄分頁
     */
    Page<AuditLog> findByTimestampBetween(
            LocalDateTime startTime, LocalDateTime endTime, Pageable pageable);

    /**
     * 查詢失敗的操作
     * @param success 是否成功
     * @param pageable 分頁參數
     * @return 操作紀錄分頁
     */
    Page<AuditLog> findBySuccess(Boolean success, Pageable pageable);

    /**
     * 根據使用者Email查詢
     * @param userEmail 使用者Email
     * @param pageable 分頁參數
     * @return 操作紀錄分頁
     */
    Page<AuditLog> findByUserEmailContainingIgnoreCase(String userEmail, Pageable pageable);
}