package com.example.schoolmanagementsystem.controller;

import com.example.schoolmanagementsystem.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.HashMap;
import java.util.Map;

/**
 * 測試控制器
 * 用於驗證系統是否正常運作
 */
@RestController
@RequestMapping("/api/test")
public class TestController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private RoleRepository roleRepository;

    /**
     * 系統健康檢查
     */
    @GetMapping("/health")
    public Map<String, Object> healthCheck() {
        Map<String, Object> status = new HashMap<>();
        status.put("status", "UP");
        status.put("message", "校務選課管理系統運作正常！");
        status.put("timestamp", System.currentTimeMillis());
        return status;
    }

    /**
     * 資料庫連線測試
     */
    @GetMapping("/database")
    public Map<String, Object> databaseCheck() {
        Map<String, Object> result = new HashMap<>();
        try {
            result.put("users_count", userRepository.count());
            result.put("students_count", studentRepository.count());
            result.put("courses_count", courseRepository.count());
            result.put("roles_count", roleRepository.count());
            result.put("database_status", "Connected");
            result.put("message", "資料庫連線成功！");
        } catch (Exception e) {
            result.put("database_status", "Error");
            result.put("error", e.getMessage());
        }
        return result;
    }

    /**
     * 系統資訊
     */
    @GetMapping("/info")
    public Map<String, String> systemInfo() {
        Map<String, String> info = new HashMap<>();
        info.put("application", "校務選課管理系統");
        info.put("version", "1.0.0");
        info.put("developer", "Your Name");
        info.put("framework", "Spring Boot 3.x");
        info.put("java_version", System.getProperty("java.version"));
        return info;
    }
}