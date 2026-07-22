package com.hospital.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReceptionistDashboardResponse {

    private long totalPatients;

    private long todayRegisteredPatients;

    private long totalAppointments;

    private long pendingAppointments;

    private long approvedAppointments;

    private long completedAppointments;

    private long cancelledAppointments;
}