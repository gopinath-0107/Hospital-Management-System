package com.hospital.controller;

import com.hospital.dto.DoctorResponse;
import com.hospital.service.DoctorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
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


    @GetMapping
    @Operation(summary = "Get Doctor By Department & Specilization")
    public ResponseEntity<List<DoctorResponse>> getDoctors(

            @RequestParam Long departmentId,
            @RequestParam Long specializationId

    ) {

        return ResponseEntity.ok(
                doctorService.getDoctors(
                        departmentId,
                        specializationId
                )
        );
    }


    @GetMapping("/all")
    @Operation(summary = "Get All Doctors")
    public ResponseEntity<List<DoctorResponse>> getAllDoctors() {

        return ResponseEntity.ok(
                doctorService.getAllDoctors()
        );
    }

    @GetMapping("/available")
    public ResponseEntity<List<DoctorResponse>> getAvailableDoctors(
            @RequestParam LocalDate date,
            @RequestParam Long departmentId,
            @RequestParam Long specializationId) {

        return ResponseEntity.ok(
                doctorService.getAvailableDoctors(
                        date,
                        departmentId,
                        specializationId
                )
        );
    }
}
