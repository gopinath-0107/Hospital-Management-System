package com.hospital.dto;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class LabDashboardResponse {


    private long totalLabOrders;


    private long pendingTests;


    private long completedReports;


    private long totalReports;

}