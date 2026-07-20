package com.hospital.entity;

import com.hospital.enums.MedicineCategory;
import com.hospital.enums.Status;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "medicines")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Medicine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "medicine_code", unique = true, nullable = false, length = 20)
    private String medicineCode;

    @Column(name = "medicine_name", nullable = false, length = 100)
    private String medicineName;

    @Column(nullable = false, length = 100)
    private String company;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MedicineCategory category;

    @Column(length = 500)
    private String description;

    @Column(nullable = false)
    @Builder.Default
    private Integer stockQuantity = 0;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(length = 50)
    private String batchNumber;

    private LocalDate manufacturingDate;

    @Column(nullable = false)
    private LocalDate expiryDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private Status status = Status.ACTIVE;
}