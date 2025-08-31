package com.example.schoolmanagementsystem.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 學生實體類別
 * 儲存學生的基本資訊
 */
@Entity
@Table(name = "Students")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Student {

    @Id
    @Column(name = "student_id", length = 20)
    private String studentId;

    @Column(name = "student_name", nullable = false)
    private String studentName;

    // 一個學生對應一個使用者帳號
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", unique = true, nullable = false)
    private User user;

}