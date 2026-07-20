package com.hospital.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class AdminDashboardResponse {


    private long totalPatients;

    private long totalDoctors;

    private long totalNurses;

    private long totalReceptionists;

    private long totalLabTechnicians;

    private long totalPharmacists;

    private long totalAppointments;

    private long totalMedicines;

    private BigDecimal totalRevenue;

}