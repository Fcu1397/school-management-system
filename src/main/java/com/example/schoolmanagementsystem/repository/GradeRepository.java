package com.example.schoolmanagementsystem.repository;

import com.example.schoolmanagementsystem.model.Grade;
import com.example.schoolmanagementsystem.model.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * 成績資料存取介面
 * 提供成績相關的資料庫操作
 */
@Repository
public interface GradeRepository extends JpaRepository<Grade, Integer> {

    /**
     * 根據選課記錄查詢成績
     * @param enrollment 選課記錄實體
     * @return 成績實體（Optional）
     */
    Optional<Grade> findByEnrollment(Enrollment enrollment);

    /**
     * 根據選課記錄ID查詢成績
     * @param enrollmentId 選課記錄ID
     * @return 成績實體（Optional）
     */
    Optional<Grade> findByEnrollment_EnrollmentId(Integer enrollmentId);

    /**
     * 查詢特定學生的所有成績
     * @param studentId 學生ID
     * @return 成績列表
     */
    @Query("SELECT g FROM Grade g JOIN g.enrollment e WHERE e.student.studentId = :studentId")
    List<Grade> findByStudentId(@Param("studentId") String studentId);

    /**
     * 查詢特定班級的所有成績
     * @param classId 班級ID
     * @return 成績列表
     */
    @Query("SELECT g FROM Grade g JOIN g.enrollment e WHERE e.classInfo.classId = :classId")
    List<Grade> findByClassId(@Param("classId") Integer classId);

    /**
     * 查詢成績在特定範圍內的記錄
     * @param minScore 最低分
     * @param maxScore 最高分
     * @return 成績列表
     */
    List<Grade> findByScoreNumericBetween(BigDecimal minScore, BigDecimal maxScore);

    /**
     * 計算特定班級的平均成績
     * @param classId 班級ID
     * @return 平均成績
     */
    @Query("SELECT AVG(g.scoreNumeric) FROM Grade g JOIN g.enrollment e WHERE e.classInfo.classId = :classId")
    BigDecimal calculateAverageScoreByClassId(@Param("classId") Integer classId);
}