package com.example.schoolmanagementsystem.repository;

import com.example.schoolmanagementsystem.model.Teacher;
import com.example.schoolmanagementsystem.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/**
 * 教師資料存取介面
 * 提供教師相關的資料庫操作
 */
@Repository
public interface TeacherRepository extends JpaRepository<Teacher, String> {

    /**
     * 根據使用者帳號查詢教師
     * @param user 使用者實體
     * @return 教師實體（Optional）
     */
    Optional<Teacher> findByUser(User user);

    /**
     * 根據使用者ID查詢教師
     * @param userId 使用者ID
     * @return 教師實體（Optional）
     */
    Optional<Teacher> findByUser_UserId(Integer userId);

    /**
     * 根據教師姓名模糊查詢
     * @param name 教師姓名（部分）
     * @return 教師列表
     */
    java.util.List<Teacher> findByTeacherNameContaining(String name);

    /**
     * 檢查某使用者是否已關聯教師資料
     * @param user 使用者實體
     * @return 是否存在
     */
    boolean existsByUser(User user);
}