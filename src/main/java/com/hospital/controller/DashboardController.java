package com.hospital.controller;


import com.hospital.dto.*;
import com.hospital.service.DashboardService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {


    private final DashboardService dashboardService;



    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AdminDashboardResponse> getAdminDashboard(){

        return ResponseEntity.ok(
                dashboardService.getAdminDashboard()
        );
    }



    @GetMapping("/doctor/{doctorId}")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<DoctorDashboardResponse> getDoctorDashboard(
            @PathVariable Long doctorId
    ){

        return ResponseEntity.ok(
                dashboardService.getDoctorDashboard(doctorId)
        );
    }




    @GetMapping("/pharmacy")
    @PreAuthorize("hasRole('PHARMACIST')")
    public ResponseEntity<PharmacyDashboardResponse> getPharmacyDashboard(){

        return ResponseEntity.ok(
                dashboardService.getPharmacyDashboard()
        );
    }




    @GetMapping("/lab")
    @PreAuthorize("hasRole('LAB_TECHNICIAN')")
    public ResponseEntity<LabDashboardResponse> getLabDashboard(){

        return ResponseEntity.ok(
                dashboardService.getLabDashboard()
        );
    }

}