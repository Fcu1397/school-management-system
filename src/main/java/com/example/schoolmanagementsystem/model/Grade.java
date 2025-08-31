package com.example.schoolmanagementsystem.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

/**
 * 成績實體類別
 * 記錄學生的課程成績
 */
@Entity
@Table(name = "Grades")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Grade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "grade_id")
    private Integer gradeId;

    @Column(name = "score_numeric", precision = 4, scale = 1)
    private BigDecimal scoreNumeric;

    // 一個成績對應一個選課記錄
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "enrollment_id", unique = true, nullable = false)
    private Enrollment enrollment;
}