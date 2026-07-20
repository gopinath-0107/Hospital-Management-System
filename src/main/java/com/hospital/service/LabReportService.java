package com.hospital.service;

import com.hospital.dto.LabReportResponse;
import com.hospital.dto.LabReportReviewRequest;
import com.hospital.dto.UploadLabReportRequest;

public interface LabReportService {


    LabReportResponse uploadReport(
            UploadLabReportRequest request
    );


    LabReportResponse getReportByLabOrderId(
            Long labOrderId
    );

    void reviewReport(
            Long reportId,
            LabReportReviewRequest request
    );

}