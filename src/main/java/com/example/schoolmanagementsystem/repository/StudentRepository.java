package com.example.schoolmanagementsystem.repository;

import com.example.schoolmanagementsystem.model.Student;
import com.example.schoolmanagementsystem.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/**
 * 學生資料存取介面
 * 提供學生相關的資料庫操作
 */
@Repository
public interface StudentRepository extends JpaRepository<Student, String> {

    /**
     * 根據使用者帳號查詢學生
     * @param user 使用者實體
     * @return 學生實體（Optional）
     */
    Optional<Student> findByUser(User user);

    /**
     * 根據使用者ID查詢學生
     * @param userId 使用者ID
     * @return 學生實體（Optional）
     */
    Optional<Student> findByUser_UserId(Integer userId);

    /**
     * 根據學生姓名模糊查詢
     * @param name 學生姓名（部分）
     * @return 學生列表
     */
    java.util.List<Student> findByStudentNameContaining(String name);

    /**
     * 檢查某使用者是否已關聯學生資料
     * @param user 使用者實體
     * @return 是否存在
     */
    boolean existsByUser(User user);
}