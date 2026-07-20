package com.hospital.controller;

import com.hospital.dto.UserResponse;
import com.hospital.service.PatientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patients")
@RequiredArgsConstructor
@Tag(name = "Patient Module", description = "Endpoints for searching and loading patient profile data.")
public class PatientController {

    private final PatientService patientService;

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('PATIENT', 'RECEPTIONIST', 'ADMIN')")
    @Operation(summary = "View Patient profile details (PATIENT/RECEPTIONIST/ADMIN)")
    public ResponseEntity<UserResponse> getPatientProfile(@PathVariable Long id) {
        UserResponse response = patientService.getPatientProfile(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('RECEPTIONIST', 'ADMIN')")
    @Operation(summary = "Search Patients by first name, last name, email, or mobile (RECEPTIONIST/ADMIN)")
    public ResponseEntity<List<UserResponse>> searchPatients(@RequestParam("query") String query) {
        List<UserResponse> response = patientService.searchPatients(query);
        return ResponseEntity.ok(response);
    }
}
