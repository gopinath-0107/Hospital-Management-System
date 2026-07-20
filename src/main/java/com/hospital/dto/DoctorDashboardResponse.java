package com.hospital.dto;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class DoctorDashboardResponse {


    private long totalAppointments;

    private long totalPatients;

    private long totalConsultations;

    private long totalPrescriptions;

}