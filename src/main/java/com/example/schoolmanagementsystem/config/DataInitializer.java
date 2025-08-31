package com.example.schoolmanagementsystem.config;

import com.example.schoolmanagementsystem.model.*;
import com.example.schoolmanagementsystem.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private final UserRepository userRepository;
    private final TeacherRepository teacherRepository;
    private final ClassInfoRepository classInfoRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        log.info("開始初始化資料...");

        // 初始化角色
        initializeRoles();

        // 初始化課程
        initializeCourses();

        // 初始化教師
        initializeTeachers();

        // 初始化班級
        initializeClasses();

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

            Course cs201 = Course.builder()
                    .courseId("CS201")
                    .courseName("資料結構")
                    .credits(new BigDecimal("3.0"))
                    .build();
            courseRepository.save(cs201);

            Course cs202 = Course.builder()
                    .courseId("CS202")
                    .courseName("演算法")
                    .credits(new BigDecimal("3.0"))
                    .build();
            courseRepository.save(cs202);

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

            log.info("建立了 {} 門課程", 6);

        } else {
            log.info("課程資料已存在，跳過初始化");
        }
    }

    /**
     * 初始化教師資料
     */
    private void initializeTeachers() {
        // 檢查是否已有教師資料
        if (teacherRepository.count() == 0) {
            log.info("初始化教師資料...");

            Role teacherRole = roleRepository.findByRoleName("TEACHER")
                    .orElseThrow(() -> new RuntimeException("找不到 TEACHER 角色"));

            // 建立教師帳號 1
            User teacherUser1 = User.builder()
                    .email("teacher1@school.edu")
                    .passwordHash(passwordEncoder.encode("Teacher123"))
                    .role(teacherRole)
                    .build();
            userRepository.save(teacherUser1);

            Teacher teacher1 = Teacher.builder()
                    .teacherId("T001")
                    .teacherName("王教授")
                    .user(teacherUser1)
                    .build();
            teacherRepository.save(teacher1);

            // 建立教師帳號 2
            User teacherUser2 = User.builder()
                    .email("teacher2@school.edu")
                    .passwordHash(passwordEncoder.encode("Teacher123"))
                    .role(teacherRole)
                    .build();
            userRepository.save(teacherUser2);

            Teacher teacher2 = Teacher.builder()
                    .teacherId("T002")
                    .teacherName("李教授")
                    .user(teacherUser2)
                    .build();
            teacherRepository.save(teacher2);

            // 建立教師帳號 3
            User teacherUser3 = User.builder()
                    .email("teacher3@school.edu")
                    .passwordHash(passwordEncoder.encode("Teacher123"))
                    .role(teacherRole)
                    .build();
            userRepository.save(teacherUser3);

            Teacher teacher3 = Teacher.builder()
                    .teacherId("T003")
                    .teacherName("張教授")
                    .user(teacherUser3)
                    .build();
            teacherRepository.save(teacher3);

            log.info("建立了 3 位教師");

        } else {
            log.info("教師資料已存在，跳過初始化");
        }
    }

    /**
     * 初始化班級資料
     */
    private void initializeClasses() {
        // 檢查是否已有班級資料
        if (classInfoRepository.count() == 0) {
            log.info("初始化班級資料...");

            // 取得教師
            Teacher teacher1 = teacherRepository.findById("T001").orElse(null);
            Teacher teacher2 = teacherRepository.findById("T002").orElse(null);
            Teacher teacher3 = teacherRepository.findById("T003").orElse(null);

            if (teacher1 == null || teacher2 == null || teacher3 == null) {
                log.warn("找不到教師資料，跳過班級初始化");
                return;
            }

            // CS101 - 計算機概論（兩個班）
            Course cs101 = courseRepository.findById("CS101").orElse(null);
            if (cs101 != null) {
                ClassInfo class1 = ClassInfo.builder()
                        .course(cs101)
                        .teacher(teacher1)
                        .academicYear(2024)
                        .semester("上學期")
                        .capacity(50)
                        .build();
                classInfoRepository.save(class1);

                ClassInfo class2 = ClassInfo.builder()
                        .course(cs101)
                        .teacher(teacher2)
                        .academicYear(2024)
                        .semester("上學期")
                        .capacity(50)
                        .build();
                classInfoRepository.save(class2);
            }

            // CS102 - 程式設計
            Course cs102 = courseRepository.findById("CS102").orElse(null);
            if (cs102 != null) {
                ClassInfo class3 = ClassInfo.builder()
                        .course(cs102)
                        .teacher(teacher1)
                        .academicYear(2024)
                        .semester("上學期")
                        .capacity(40)
                        .build();
                classInfoRepository.save(class3);
            }

            // CS201 - 資料結構
            Course cs201 = courseRepository.findById("CS201").orElse(null);
            if (cs201 != null) {
                ClassInfo class4 = ClassInfo.builder()
                        .course(cs201)
                        .teacher(teacher2)
                        .academicYear(2024)
                        .semester("上學期")
                        .capacity(35)
                        .build();
                classInfoRepository.save(class4);
            }

            // MATH101 - 微積分
            Course math101 = courseRepository.findById("MATH101").orElse(null);
            if (math101 != null) {
                ClassInfo class5 = ClassInfo.builder()
                        .course(math101)
                        .teacher(teacher3)
                        .academicYear(2024)
                        .semester("上學期")
                        .capacity(60)
                        .build();
                classInfoRepository.save(class5);
            }

            // ENG101 - 英文寫作
            Course eng101 = courseRepository.findById("ENG101").orElse(null);
            if (eng101 != null) {
                ClassInfo class6 = ClassInfo.builder()
                        .course(eng101)
                        .teacher(teacher3)
                        .academicYear(2024)
                        .semester("上學期")
                        .capacity(30)
                        .build();
                classInfoRepository.save(class6);
            }

            log.info("建立了 6 個班級");

        } else {
            log.info("班級資料已存在，跳過初始化");
        }
    }
}