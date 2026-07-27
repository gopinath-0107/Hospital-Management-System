package com.hospital.service;

import com.hospital.dto.LabReportResponse;
import com.hospital.dto.LabReportReviewRequest;
import com.hospital.dto.UploadLabReportRequest;

import java.util.List;

public interface LabReportService {

    LabReportResponse uploadReport(
            UploadLabReportRequest request
    );

    LabReportResponse getReportByLabOrderId(
            Long labOrderId
    );

    // Doctor - View Reports
    List<LabReportResponse> getReportsByDoctor(
            Long doctorId
    );

    // Patient - View Reports
    List<LabReportResponse> getReportsByPatient(
            Long patientId
    );

    // Lab Technician - View Uploaded Reports
    List<LabReportResponse> getReportsByLabTechnician(
            Long labTechnicianId
    );

    void reviewReport(
            Long reportId,
            LabReportReviewRequest request
    );

}