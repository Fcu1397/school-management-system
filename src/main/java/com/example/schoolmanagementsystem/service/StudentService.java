package com.example.schoolmanagementsystem.service;

import com.example.schoolmanagementsystem.dto.request.EnrollmentRequest;
import com.example.schoolmanagementsystem.dto.response.EnrollmentResponse;
import com.example.schoolmanagementsystem.dto.response.StudentResponse;
import java.util.List;

/**
 * 學生服務介面
 * 定義學生相關的業務邏輯
 */
public interface StudentService {

    /**
     * 取得學生詳細資訊
     * @param studentId 學號
     * @return 學生資訊
     */
    StudentResponse getStudentInfo(String studentId);

    /**
     * 選課
     * @param request 選課請求
     * @return 選課結果
     */
    EnrollmentResponse enrollInClass(EnrollmentRequest request);

    /**
     * 退選
     * @param studentId 學號
     * @param classId 班級ID
     * @return 是否成功
     */
    boolean dropClass(String studentId, Integer classId);

    /**
     * 查詢學生的選課清單
     * @param studentId 學號
     * @return 選課清單
     */
    List<EnrollmentResponse> getEnrollments(String studentId);

    /**
     * 查詢學生的選課清單（特定學期）
     * @param studentId 學號
     * @param academicYear 學年
     * @param semester 學期
     * @return 選課清單
     */
    List<EnrollmentResponse> getEnrollmentsBySemester(
            String studentId, Integer academicYear, String semester);

    /**
     * 檢查是否有選課衝堂
     * @param studentId 學號
     * @param classId 班級ID
     * @return 是否衝堂
     */
    boolean hasScheduleConflict(String studentId, Integer classId);
}