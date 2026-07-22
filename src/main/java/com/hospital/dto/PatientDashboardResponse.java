package com.hospital.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PatientDashboardResponse {

    private long totalAppointments;

    private long upcomingAppointments;

    private long completedAppointments;

    private long cancelledAppointments;

    private long totalPrescriptions;

    private long totalLabTests;

    private long totalLabReports;

    private long totalBills;

    private long paidBills;
}