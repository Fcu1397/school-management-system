package com.example.schoolmanagementsystem.service.impl;

import com.example.schoolmanagementsystem.dto.request.LoginRequest;
import com.example.schoolmanagementsystem.dto.request.UserRegistrationRequest;
import com.example.schoolmanagementsystem.dto.response.UserResponse;
import com.example.schoolmanagementsystem.model.*;
import com.example.schoolmanagementsystem.repository.*;
import com.example.schoolmanagementsystem.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 認證服務實作類別
 * 實作使用者認證相關的業務邏輯
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final PasswordEncoder passwordEncoder;
    private final LoginHistoryRepository loginHistoryRepository;

    @Override
    @Transactional
    public UserResponse registerUser(UserRegistrationRequest request) {
        log.info("開始註冊新使用者: {}", request.getEmail());

        // 1. 驗證密碼是否一致
        if (!request.isPasswordMatch()) {
            throw new IllegalArgumentException("密碼與確認密碼不一致");
        }

        // 2. 檢查 Email 是否已存在
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("此 Email 已被註冊");
        }

        // 3. 檢查學號或教職員編號是否已存在
        if ("STUDENT".equals(request.getRoleName()) && request.getStudentId() != null) {
            if (studentRepository.existsById(request.getStudentId())) {
                throw new IllegalArgumentException("此學號已被註冊");
            }
        } else if ("TEACHER".equals(request.getRoleName()) && request.getTeacherId() != null) {
            if (teacherRepository.existsById(request.getTeacherId())) {
                throw new IllegalArgumentException("此教職員編號已被註冊");
            }
        }

        // 4. 取得角色
        Role role = roleRepository.findByRoleName(request.getRoleName())
                .orElseThrow(() -> new IllegalArgumentException("無效的角色: " + request.getRoleName()));

        // 5. 建立使用者帳號
        User user = User.builder()
                .email(request.getEmail())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .role(role)
                .build();
        user = userRepository.save(user);

        // 6. 根據角色建立對應的資料
        if ("STUDENT".equals(request.getRoleName())) {
            Student student = Student.builder()
                    .studentId(request.getStudentId())
                    .studentName(request.getName())
                    .user(user)
                    .build();
            studentRepository.save(student);
        } else if ("TEACHER".equals(request.getRoleName())) {
            Teacher teacher = Teacher.builder()
                    .teacherId(request.getTeacherId())
                    .teacherName(request.getName())
                    .user(user)
                    .build();
            teacherRepository.save(teacher);
        }

        log.info("使用者註冊成功: {}", user.getEmail());

        // 7. 建立回應 DTO
        return UserResponse.builder()
                .userId(user.getUserId())
                .email(user.getEmail())
                .roleName(role.getRoleName())
                .name(request.getName())
                .identifier(request.getStudentId() != null ? request.getStudentId() : request.getTeacherId())
                .department(request.getDepartment())
                .isActive(true)
                .isLocked(false)
                .build();
    }

    @Override
    @Transactional
    public UserResponse login(LoginRequest request) {
        log.info("使用者嘗試登入: {}", request.getEmail());

        // 1. 查詢使用者
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Email 或密碼錯誤"));

        // 2. 驗證密碼
        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            // 記錄登入失敗
            recordLoginHistory(user, false, "密碼錯誤");
            throw new IllegalArgumentException("Email 或密碼錯誤");
        }

        // 3. 記錄登入成功
        recordLoginHistory(user, true, null);

        // 4. 取得使用者詳細資訊
        String name = "";
        String identifier = "";

        if ("STUDENT".equals(user.getRole().getRoleName())) {
            Student student = studentRepository.findByUser(user)
                    .orElse(null);
            if (student != null) {
                name = student.getStudentName();
                identifier = student.getStudentId();
            }
        } else if ("TEACHER".equals(user.getRole().getRoleName())) {
            Teacher teacher = teacherRepository.findByUser(user)
                    .orElse(null);
            if (teacher != null) {
                name = teacher.getTeacherName();
                identifier = teacher.getTeacherId();
            }
        }

        log.info("使用者登入成功: {}", user.getEmail());

        // 5. 建立回應 DTO
        return UserResponse.builder()
                .userId(user.getUserId())
                .email(user.getEmail())
                .roleName(user.getRole().getRoleName())
                .name(name)
                .identifier(identifier)
                .isActive(true)
                .isLocked(false)
                .build();
    }

    @Override
    public boolean isEmailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public boolean isStudentIdExists(String studentId) {
        return studentRepository.existsById(studentId);
    }

    @Override
    public boolean isTeacherIdExists(String teacherId) {
        return teacherRepository.existsById(teacherId);
    }

    /**
     * 記錄登入歷史
     */
    private void recordLoginHistory(User user, boolean success, String failureReason) {
        LoginHistory history = LoginHistory.builder()
                .userId(user.getUserId())
                .email(user.getEmail())
                .loginTime(java.time.LocalDateTime.now())
                .loginStatus(success ? "SUCCESS" : "FAILED")
                .failureReason(failureReason)
                .ipAddress("127.0.0.1") // TODO: 從 Request 中取得實際 IP
                .build();
        loginHistoryRepository.save(history);
    }
}