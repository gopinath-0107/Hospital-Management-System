package com.hospital.controller;


import com.hospital.dto.LabReportResponse;
import com.hospital.dto.LabReportReviewRequest;
import com.hospital.dto.UploadLabReportRequest;
import com.hospital.service.LabReportService;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/api/lab-reports")
@RequiredArgsConstructor
public class LabReportController {



    private final LabReportService labReportService;



    // Upload Lab Report (Lab Technician)

    @PostMapping(
            value="/upload",
            consumes="multipart/form-data"
    )
    public ResponseEntity<LabReportResponse> uploadReport(

            @RequestParam Long labOrderId,

            @RequestParam MultipartFile file,

            @RequestParam String report

    ){

        UploadLabReportRequest request =
                new UploadLabReportRequest();


        request.setLabOrderId(labOrderId);

        request.setFile(file);

        request.setReport(report);



        return ResponseEntity.ok(
                labReportService.uploadReport(request)
        );

    }



    // Get Report By Lab Order Id (Doctor)

    @GetMapping("/order/{labOrderId}")
    public ResponseEntity<LabReportResponse> getReportByLabOrderId(
            @PathVariable Long labOrderId) {


        return ResponseEntity.ok(
                labReportService
                        .getReportByLabOrderId(labOrderId)
        );

    }

    @PutMapping("/{id}/review")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<String> reviewReport(
            @PathVariable Long id,
            @RequestBody LabReportReviewRequest request){


        labReportService.reviewReport(id,request);


        return ResponseEntity.ok(
                "Report reviewed successfully"
        );

    }

}