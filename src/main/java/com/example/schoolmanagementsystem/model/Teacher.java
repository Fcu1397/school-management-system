package com.example.schoolmanagementsystem.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 教師實體類別
 * 儲存教師的基本資訊
 */
@Entity
@Table(name = "Teachers")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Teacher {

    @Id
    @Column(name = "teacher_id", length = 20)
    private String teacherId;

    @Column(name = "teacher_name", nullable = false)
    private String teacherName;

    // 一個教師對應一個使用者帳號
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", unique = true, nullable = false)
    private User user;

}