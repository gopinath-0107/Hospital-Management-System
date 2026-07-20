package com.hospital.service;

import com.hospital.dto.AdminDashboardResponse;
import com.hospital.dto.DoctorDashboardResponse;
import com.hospital.dto.LabDashboardResponse;
import com.hospital.dto.PharmacyDashboardResponse;

public interface DashboardService {

    AdminDashboardResponse getAdminDashboard();

    DoctorDashboardResponse getDoctorDashboard(Long doctorId);

    PharmacyDashboardResponse getPharmacyDashboard();

    LabDashboardResponse getLabDashboard();



}