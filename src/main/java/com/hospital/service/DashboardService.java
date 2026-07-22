package com.hospital.service;

import com.hospital.dto.*;

public interface DashboardService {

    AdminDashboardResponse getAdminDashboard();

    DoctorDashboardResponse getDoctorDashboard(Long doctorId);

    PharmacyDashboardResponse getPharmacyDashboard();

    LabDashboardResponse getLabDashboard();

    PatientDashboardResponse getPatientDashboard(Long patientId);

    ReceptionistDashboardResponse getReceptionistDashboard();

}