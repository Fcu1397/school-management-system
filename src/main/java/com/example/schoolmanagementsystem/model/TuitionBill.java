package com.example.schoolmanagementsystem.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 學費帳單實體類別
 * 記錄學生的學費繳納資訊
 */
@Entity
@Table(name = "TuitionBills")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TuitionBill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bill_id")
    private Integer billId;

    @Column(name = "academic_year", nullable = false)
    private Integer academicYear;

    @Column(name = "semester", length = 50, nullable = false)
    private String semester;

    @Column(name = "amount", precision = 10, scale = 2, nullable = false)
    private BigDecimal amount;

    @Column(name = "due_date", nullable = false)
    private LocalDate dueDate;

    @Column(name = "status", length = 20, nullable = false)
    private String status; // 未繳費、已繳費、逾期

    @Column(name = "paid_date")
    private LocalDate paidDate;

    @Column(name = "payment_method", length = 50)
    private String paymentMethod; // 現金、信用卡、轉帳

    // 多個帳單對應一個學生
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;
}