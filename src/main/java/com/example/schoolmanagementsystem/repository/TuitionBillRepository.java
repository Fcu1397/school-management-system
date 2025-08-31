package com.example.schoolmanagementsystem.repository;

import com.example.schoolmanagementsystem.model.TuitionBill;
import com.example.schoolmanagementsystem.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * 學費帳單資料存取介面
 * 提供學費相關的資料庫操作
 */
@Repository
public interface TuitionBillRepository extends JpaRepository<TuitionBill, Integer> {

    /**
     * 查詢學生的所有帳單
     * @param student 學生實體
     * @return 帳單列表
     */
    List<TuitionBill> findByStudent(Student student);

    /**
     * 根據學生ID查詢帳單
     * @param studentId 學生ID
     * @return 帳單列表
     */
    List<TuitionBill> findByStudent_StudentId(String studentId);

    /**
     * 查詢特定學年學期的帳單
     * @param studentId 學生ID
     * @param academicYear 學年
     * @param semester 學期
     * @return 帳單（Optional）
     */
    Optional<TuitionBill> findByStudent_StudentIdAndAcademicYearAndSemester(
            String studentId, Integer academicYear, String semester);

    /**
     * 查詢未繳費的帳單
     * @param studentId 學生ID
     * @return 未繳費帳單列表
     */
    List<TuitionBill> findByStudent_StudentIdAndStatus(String studentId, String status);

    /**
     * 查詢逾期未繳的帳單
     * @param currentDate 當前日期
     * @return 逾期帳單列表
     */
    @Query("SELECT t FROM TuitionBill t WHERE t.status = '未繳費' AND t.dueDate < :currentDate")
    List<TuitionBill> findOverdueBills(@Param("currentDate") LocalDate currentDate);

    /**
     * 統計學生未繳費總額
     * @param studentId 學生ID
     * @return 未繳費總額
     */
    @Query("SELECT SUM(t.amount) FROM TuitionBill t WHERE t.student.studentId = :studentId AND t.status = '未繳費'")
    java.math.BigDecimal calculateUnpaidAmount(@Param("studentId") String studentId);
}