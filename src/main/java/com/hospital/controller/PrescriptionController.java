package com.hospital.controller;

import com.hospital.dto.PrescriptionRequest;
import com.hospital.dto.PrescriptionResponse;
import com.hospital.enums.PrescriptionStatus;
import com.hospital.service.PrescriptionService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/prescriptions")
@RequiredArgsConstructor
public class PrescriptionController {


    private final PrescriptionService prescriptionService;



    // Doctor Create Prescription
    @PostMapping
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<PrescriptionResponse> createPrescription(
            @Valid @RequestBody PrescriptionRequest request) {


        PrescriptionResponse response =
                prescriptionService.createPrescription(request);


        return new ResponseEntity<>(
                response,
                HttpStatus.CREATED
        );
    }



    // Get Prescription By Id
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('DOCTOR','PATIENT')")
    public ResponseEntity<PrescriptionResponse> getById(
            @PathVariable Long id) {


        return ResponseEntity.ok(
                prescriptionService.getPrescriptionById(id)
        );
    }




    // Appointment wise prescription
    @GetMapping("/appointment/{appointmentId}")
    @PreAuthorize("hasAnyRole('DOCTOR','PATIENT')")
    public ResponseEntity<PrescriptionResponse> getByAppointment(
            @PathVariable Long appointmentId) {


        return ResponseEntity.ok(
                prescriptionService
                        .getPrescriptionByAppointment(appointmentId)
        );

    }




    // Admin/Doctor view all
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<PrescriptionResponse>> getAll(){


        return ResponseEntity.ok(
                prescriptionService.getAllPrescriptions()
        );

    }


    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('PHARMACIST')")
    public ResponseEntity<String> updateStatus(
            @PathVariable Long id,
            @RequestParam PrescriptionStatus status){


        prescriptionService
                .updatePrescriptionStatus(id,status);


        return ResponseEntity.ok(
                "Prescription status updated"
        );
    }

}