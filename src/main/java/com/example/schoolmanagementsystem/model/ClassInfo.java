package com.example.schoolmanagementsystem.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 開課班級實體類別
 * 儲存每學期開設的課程班級資訊
 * 注意：使用 ClassInfo 而非 Class，避免與 Java 關鍵字衝突
 */
@Entity
@Table(name = "Classes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClassInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "class_id")
    private Integer classId;

    @Column(name = "academic_year", nullable = false)
    private Integer academicYear;

    @Column(name = "semester", length = 50, nullable = false)
    private String semester;

    @Column(name = "capacity", nullable = false)
    private Integer capacity;

    // 多個班級對應一個課程
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    // 多個班級對應一個教師
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id", nullable = false)
    private Teacher teacher;

}