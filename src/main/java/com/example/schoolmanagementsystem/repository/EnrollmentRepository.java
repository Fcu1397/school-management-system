package com.example.schoolmanagementsystem.repository;

import com.example.schoolmanagementsystem.model.Enrollment;
import com.example.schoolmanagementsystem.model.Student;
import com.example.schoolmanagementsystem.model.ClassInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

/**
 * 選課記錄資料存取介面
 * 提供選課相關的資料庫操作
 */
@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Integer> {

    /**
     * 查詢學生的所有選課記錄
     * @param student 學生實體
     * @return 選課記錄列表
     */
    List<Enrollment> findByStudent(Student student);

    /**
     * 查詢班級的所有選課記錄
     * @param classInfo 班級實體
     * @return 選課記錄列表
     */
    List<Enrollment> findByClassInfo(ClassInfo classInfo);

    /**
     * 根據學生ID查詢選課記錄
     * @param studentId 學生ID
     * @return 選課記錄列表
     */
    List<Enrollment> findByStudent_StudentId(String studentId);

    /**
     * 根據班級ID查詢選課記錄
     * @param classId 班級ID
     * @return 選課記錄列表
     */
    List<Enrollment> findByClassInfo_ClassId(Integer classId);

    /**
     * 查詢特定學生在特定班級的選課記錄
     * @param student 學生實體
     * @param classInfo 班級實體
     * @return 選課記錄（Optional）
     */
    Optional<Enrollment> findByStudentAndClassInfo(Student student, ClassInfo classInfo);

    /**
     * 檢查學生是否已選修某班級
     * @param studentId 學生ID
     * @param classId 班級ID
     * @return 是否已選修
     */
    boolean existsByStudent_StudentIdAndClassInfo_ClassId(String studentId, Integer classId);

    /**
     * 計算班級目前選課人數
     * @param classId 班級ID
     * @return 選課人數
     */
    @Query("SELECT COUNT(e) FROM Enrollment e WHERE e.classInfo.classId = :classId")
    Long countByClassId(@Param("classId") Integer classId);
}