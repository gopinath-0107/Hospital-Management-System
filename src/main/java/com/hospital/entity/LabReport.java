package com.hospital.entity;


import com.hospital.enums.LabReportStatus;

import jakarta.persistence.*;

import lombok.Data;

import java.time.LocalDateTime;



@Entity
@Data
public class LabReport {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lab_technician_id")
    private LabTechnician labTechnician;

    @OneToOne
    @JoinColumn(name = "lab_order_id")
    private LabOrder labOrder;



    private String report;


    private String filePath;


    @Enumerated(EnumType.STRING)
    private LabReportStatus status;


    @Column(length = 1000)
    private String doctorRemarks;


    private boolean reviewed;


    private LocalDateTime reviewedAt;

    private LocalDateTime createdAt;


    private LocalDateTime updatedAt;

}