package com.hospital.controller;

import com.hospital.dto.LabReportResponse;
import com.hospital.dto.LabReportReviewRequest;
import com.hospital.dto.UploadLabReportRequest;
import com.hospital.service.LabReportService;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/lab-reports")
@RequiredArgsConstructor
public class LabReportController {

    private final LabReportService labReportService;

    // ==========================================
    // Upload Lab Report (Lab Technician)
    // ==========================================

    @PostMapping(
            value = "/upload",
            consumes = "multipart/form-data"
    )
    @PreAuthorize("hasRole('LAB_TECHNICIAN')")
    public ResponseEntity<LabReportResponse> uploadReport(

            @Valid
            @RequestParam Long labOrderId,

            @RequestParam MultipartFile file,

            @RequestParam String report

    ) {

        UploadLabReportRequest request =
                new UploadLabReportRequest();

        request.setLabOrderId(labOrderId);
        request.setFile(file);
        request.setReport(report);

        return ResponseEntity.ok(
                labReportService.uploadReport(request)
        );
    }

    // ==========================================
    // Get Report By Lab Order Id
    // ==========================================

    @GetMapping("/order/{labOrderId}")
    @PreAuthorize("hasAnyRole('DOCTOR','PATIENT','LAB_TECHNICIAN')")
    public ResponseEntity<LabReportResponse> getReportByLabOrderId(
            @PathVariable Long labOrderId) {

        return ResponseEntity.ok(
                labReportService.getReportByLabOrderId(labOrderId)
        );
    }

    // ==========================================
    // Doctor - View Reports
    // ==========================================

    @GetMapping("/doctor/{doctorId}")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<List<LabReportResponse>> getReportsByDoctor(
            @PathVariable Long doctorId) {

        return ResponseEntity.ok(
                labReportService.getReportsByDoctor(doctorId)
        );
    }

    // ==========================================
    // Patient - View Reports
    // ==========================================

    @GetMapping("/patient/{patientId}")
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<List<LabReportResponse>> getReportsByPatient(
            @PathVariable Long patientId) {

        return ResponseEntity.ok(
                labReportService.getReportsByPatient(patientId)
        );
    }

    // ==========================================
    // Lab Technician - View Uploaded Reports
    // ==========================================

    @GetMapping("/lab-technician/{labTechnicianId}")
    @PreAuthorize("hasRole('LAB_TECHNICIAN')")
    public ResponseEntity<List<LabReportResponse>> getReportsByLabTechnician(
            @PathVariable Long labTechnicianId) {

        return ResponseEntity.ok(
                labReportService.getReportsByLabTechnician(labTechnicianId)
        );
    }

    // ==========================================
    // Doctor Review Report
    // ==========================================

    @PutMapping("/{id}/review")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<String> reviewReport(
            @PathVariable Long id,
            @RequestBody LabReportReviewRequest request) {

        labReportService.reviewReport(id, request);

        return ResponseEntity.ok(
                "Report reviewed successfully"
        );
    }


    @GetMapping("/{id}/download")
    @PreAuthorize("hasAnyRole('DOCTOR','PATIENT','LAB_TECHNICIAN')")
    public ResponseEntity<Resource> downloadReport(
            @PathVariable Long id) {

        Resource resource = labReportService.downloadReport(id);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "inline; filename=\"LabReport.pdf\""
                )
                .body(resource);
    }

}