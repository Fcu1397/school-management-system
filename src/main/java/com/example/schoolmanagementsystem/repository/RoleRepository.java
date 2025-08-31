package com.example.schoolmanagementsystem.repository;

import com.example.schoolmanagementsystem.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/**
 * 角色資料存取介面
 * 提供角色相關的資料庫操作
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {

    /**
     * 根據角色名稱查詢角色
     * @param roleName 角色名稱
     * @return 角色實體（Optional）
     */
    Optional<Role> findByRoleName(String roleName);

    /**
     * 檢查角色名稱是否存在
     * @param roleName 角色名稱
     * @return 是否存在
     */
    boolean existsByRoleName(String roleName);
}