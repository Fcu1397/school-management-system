-- File: src/main/resources/data.sql
-- 初始化測試資料
-- 注意：這個檔案會在每次啟動時執行

-- 插入角色資料
INSERT INTO Roles (role_name) VALUES ('STUDENT'), ('TEACHER'), ('ADMIN')
    ON DUPLICATE KEY UPDATE role_name = VALUES(role_name);

-- 插入測試用管理員帳號 (密碼: admin123)
-- 密碼是用 BCrypt 加密的 "admin123"
INSERT INTO Users (email, password_hash, role_id, full_name, phone, address, gender, created_at, updated_at)
SELECT 'admin@school.edu', '$2a$10$YourHashedPasswordHere', role_id, '系統管理員', '02-12345678', '台北市信義區', '其他', NOW(), NOW()
FROM Roles WHERE role_name = 'ADMIN'
    ON DUPLICATE KEY UPDATE email = VALUES(email), full_name = VALUES(full_name);

-- 插入測試學生帳號
INSERT INTO Users (email, password_hash, role_id, full_name, phone, address, gender, birth_date, created_at, updated_at)
SELECT 'student@school.edu', '$2a$10$YourHashedPasswordHere', role_id, '張小明', '0912345678', '新北市板橋區', '男', '2000-01-15', NOW(), NOW()
FROM Roles WHERE role_name = 'STUDENT'
    ON DUPLICATE KEY UPDATE email = VALUES(email), full_name = VALUES(full_name);

-- 插入測試教師帳號
INSERT INTO Users (email, password_hash, role_id, full_name, phone, address, gender, birth_date, created_at, updated_at)
SELECT 'teacher@school.edu', '$2a$10$YourHashedPasswordHere', role_id, '李老師', '0987654321', '台中市西區', '女', '1980-05-20', NOW(), NOW()
FROM Roles WHERE role_name = 'TEACHER'
    ON DUPLICATE KEY UPDATE email = VALUES(email), full_name = VALUES(full_name);

-- 插入測試課程
INSERT INTO Courses (course_id, course_name, credits) VALUES
                                                          ('CS101', '計算機概論', 3.0),
                                                          ('CS102', '程式設計', 3.0),
                                                          ('MATH101', '微積分', 4.0),
                                                          ('ENG101', '英文寫作', 2.0)
    ON DUPLICATE KEY UPDATE course_name = VALUES(course_name);