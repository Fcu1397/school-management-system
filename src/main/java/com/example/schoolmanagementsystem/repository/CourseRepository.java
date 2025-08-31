package com.example.schoolmanagementsystem.repository;

import com.example.schoolmanagementsystem.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.util.List;

/**
 * 課程資料存取介面
 * 提供課程相關的資料庫操作
 */
@Repository
public interface CourseRepository extends JpaRepository<Course, String> {

    /**
     * 根據課程名稱模糊查詢
     * @param courseName 課程名稱（部分）
     * @return 課程列表
     */
    List<Course> findByCourseNameContaining(String courseName);

    /**
     * 查詢特定學分數的課程
     * @param credits 學分數
     * @return 課程列表
     */
    List<Course> findByCredits(BigDecimal credits);

    /**
     * 查詢學分數範圍內的課程
     * @param minCredits 最小學分數
     * @param maxCredits 最大學分數
     * @return 課程列表
     */
    List<Course> findByCreditsBetween(BigDecimal minCredits, BigDecimal maxCredits);

    /**
     * 檢查課程ID是否存在
     * @param courseId 課程ID
     * @return 是否存在
     */
    boolean existsByCourseId(String courseId);
}