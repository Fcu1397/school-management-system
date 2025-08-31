package com.example.schoolmanagementsystem.repository;

import com.example.schoolmanagementsystem.model.User;
import com.example.schoolmanagementsystem.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

/**
 * 使用者資料存取介面
 * 提供使用者帳號相關的資料庫操作
 */
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    /**
     * 根據電子郵件查詢使用者
     * @param email 電子郵件
     * @return 使用者實體（Optional）
     */
    Optional<User> findByEmail(String email);

    /**
     * 檢查電子郵件是否已存在
     * @param email 電子郵件
     * @return 是否存在
     */
    boolean existsByEmail(String email);

    /**
     * 根據角色查詢所有使用者
     * @param role 角色實體
     * @return 使用者列表
     */
    List<User> findByRole(Role role);

    /**
     * 根據角色名稱查詢所有使用者
     * @param roleName 角色名稱
     * @return 使用者列表
     */
    List<User> findByRole_RoleName(String roleName);
}