package com.example.schoolmanagementsystem.config;

import com.example.schoolmanagementsystem.model.Course;
import com.example.schoolmanagementsystem.model.Role;
import com.example.schoolmanagementsystem.repository.CourseRepository;
import com.example.schoolmanagementsystem.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;

/**
 * 資料初始化器
 * 在應用程式啟動時自動初始化必要的資料
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final CourseRepository courseRepository;

    @Override
    public void run(String... args) throws Exception {
        log.info("開始初始化資料...");

        // 初始化角色
        initializeRoles();

        // 初始化課程
        initializeCourses();

        log.info("資料初始化完成！");
    }

    /**
     * 初始化角色資料
     */
    private void initializeRoles() {
        // 檢查是否已有角色資料
        if (roleRepository.count() == 0) {
            log.info("初始化角色資料...");

            Role studentRole = Role.builder()
                    .roleName("STUDENT")
                    .build();
            roleRepository.save(studentRole);
            log.info("建立角色: STUDENT");

            Role teacherRole = Role.builder()
                    .roleName("TEACHER")
                    .build();
            roleRepository.save(teacherRole);
            log.info("建立角色: TEACHER");

            Role adminRole = Role.builder()
                    .roleName("ADMIN")
                    .build();
            roleRepository.save(adminRole);
            log.info("建立角色: ADMIN");

        } else {
            log.info("角色資料已存在，跳過初始化");
        }
    }

    /**
     * 初始化課程資料
     */
    private void initializeCourses() {
        // 檢查是否已有課程資料
        if (courseRepository.count() == 0) {
            log.info("初始化課程資料...");

            Course cs101 = Course.builder()
                    .courseId("CS101")
                    .courseName("計算機概論")
                    .credits(new BigDecimal("3.0"))
                    .build();
            courseRepository.save(cs101);

            Course cs102 = Course.builder()
                    .courseId("CS102")
                    .courseName("程式設計")
                    .credits(new BigDecimal("3.0"))
                    .build();
            courseRepository.save(cs102);

            Course math101 = Course.builder()
                    .courseId("MATH101")
                    .courseName("微積分")
                    .credits(new BigDecimal("4.0"))
                    .build();
            courseRepository.save(math101);

            Course eng101 = Course.builder()
                    .courseId("ENG101")
                    .courseName("英文寫作")
                    .credits(new BigDecimal("2.0"))
                    .build();
            courseRepository.save(eng101);

            log.info("建立了 {} 門課程", 4);

        } else {
            log.info("課程資料已存在，跳過初始化");
        }
    }
}