package com.example.schoolmanagementsystem.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

/**
 * 課程實體類別
 * 儲存課程的基本資訊
 */
@Entity
@Table(name = "Courses")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Course {

    @Id
    @Column(name = "course_id", length = 20)
    private String courseId;

    @Column(name = "course_name", nullable = false)
    private String courseName;

    @Column(name = "credits", precision = 3, scale = 1, nullable = false)
    private BigDecimal credits;

}