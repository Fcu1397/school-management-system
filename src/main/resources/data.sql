-- File: src/main/resources/data.sql
-- 初始化測試資料
-- 注意：這個檔案會在每次啟動時執行

-- 插入角色資料
INSERT INTO Roles (role_name) VALUES ('STUDENT'), ('TEACHER'), ('ADMIN')
    ON DUPLICATE KEY UPDATE role_name = VALUES(role_name);

-- 插入測試用管理員帳號 (密碼: admin123)
-- 密碼是用 BCrypt 加密的 "admin123"
INSERT INTO Users (email, password_hash, role_id)
SELECT 'admin@school.edu', '$2a$10$YourHashedPasswordHere', role_id
FROM Roles WHERE role_name = 'ADMIN'
    ON DUPLICATE KEY UPDATE email = VALUES(email);

-- 插入測試課程
INSERT INTO Courses (course_id, course_name, credits) VALUES
                                                          ('CS101', '計算機概論', 3.0),
                                                          ('CS102', '程式設計', 3.0),
                                                          ('MATH101', '微積分', 4.0),
                                                          ('ENG101', '英文寫作', 2.0)
    ON DUPLICATE KEY UPDATE course_name = VALUES(course_name);