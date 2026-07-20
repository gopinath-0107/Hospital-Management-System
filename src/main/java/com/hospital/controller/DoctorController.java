package com.hospital.controller;

import com.hospital.dto.DoctorResponse;
import com.hospital.service.DoctorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doctors")
@RequiredArgsConstructor
@Tag(name = "Doctor Module", description = "Endpoints for viewing doctor profiles and department listings.")
public class DoctorController {

    private final DoctorService doctorService;

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('DOCTOR', 'ADMIN', 'RECEPTIONIST')")
    @Operation(summary = "View Doctor profile details (DOCTOR/ADMIN/RECEPTIONIST)")
    public ResponseEntity<DoctorResponse> getDoctorProfile(@PathVariable Long id) {
        DoctorResponse response = doctorService.getDoctorProfile(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/department/{departmentId}")
    @Operation(summary = "Get all available Doctors belonging to a specific Department (Any authenticated user)")
    public ResponseEntity<List<DoctorResponse>> getAvailableDoctors(@PathVariable Long departmentId) {
        List<DoctorResponse> response = doctorService.getAvailableDoctorsByDepartment(departmentId);
        return ResponseEntity.ok(response);
    }
}
