package com.example.schoolmanagementsystem.repository;

import com.example.schoolmanagementsystem.model.ClassInfo;
import com.example.schoolmanagementsystem.model.Course;
import com.example.schoolmanagementsystem.model.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * 開課班級資料存取介面
 * 提供班級相關的資料庫操作
 */
@Repository
public interface ClassInfoRepository extends JpaRepository<ClassInfo, Integer> {

    /**
     * 查詢特定學年學期的所有班級
     * @param academicYear 學年
     * @param semester 學期
     * @return 班級列表
     */
    List<ClassInfo> findByAcademicYearAndSemester(Integer academicYear, String semester);

    /**
     * 查詢特定課程的所有開課班級
     * @param course 課程實體
     * @return 班級列表
     */
    List<ClassInfo> findByCourse(Course course);

    /**
     * 查詢特定教師的所有授課班級
     * @param teacher 教師實體
     * @return 班級列表
     */
    List<ClassInfo> findByTeacher(Teacher teacher);

    /**
     * 查詢特定教師在特定學年學期的授課班級
     * @param teacher 教師實體
     * @param academicYear 學年
     * @param semester 學期
     * @return 班級列表
     */
    List<ClassInfo> findByTeacherAndAcademicYearAndSemester(
            Teacher teacher, Integer academicYear, String semester);

    /**
     * 根據課程ID查詢所有開課班級
     * @param courseId 課程ID
     * @return 班級列表
     */
    List<ClassInfo> findByCourse_CourseId(String courseId);
}