package com.hospital.controller;

import com.hospital.dto.DoctorAvailabilityRequest;
import com.hospital.dto.DoctorAvailabilityResponse;
import com.hospital.dto.DoctorResponse;
import com.hospital.service.DoctorAvailabilityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/doctor-availability")
@RequiredArgsConstructor
public class DoctorAvailabilityController {

    private final DoctorAvailabilityService doctorAvailabilityService;

    @PreAuthorize("hasRole('RECEPTIONIST')")
    @PostMapping
    public ResponseEntity<DoctorAvailabilityResponse> createAvailability(
            @Valid
            @RequestBody DoctorAvailabilityRequest request
    ) {

        return ResponseEntity.ok(
                doctorAvailabilityService.createAvailability(request)
        );
    }

    @PreAuthorize("hasRole('RECEPTIONIST')")
    @PutMapping("/{id}")
    public ResponseEntity<DoctorAvailabilityResponse> updateAvailability(
            @PathVariable Long id,
            @Valid
            @RequestBody DoctorAvailabilityRequest request
    ) {

        return ResponseEntity.ok(
                doctorAvailabilityService.updateAvailability(id, request)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<DoctorAvailabilityResponse> getAvailability(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                doctorAvailabilityService.getAvailability(id)
        );
    }

    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<DoctorAvailabilityResponse>> getDoctorAvailability(
            @PathVariable Long doctorId
    ) {

        return ResponseEntity.ok(
                doctorAvailabilityService.getDoctorAvailability(doctorId)
        );
    }



    @GetMapping("/date")
    public ResponseEntity<List<DoctorAvailabilityResponse>> getAvailabilityByDate(
            @RequestParam LocalDate date
    ) {

        return ResponseEntity.ok(
                doctorAvailabilityService.getAvailabilityByDate(date)
        );
    }

    @PreAuthorize("hasRole('RECEPTIONIST')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAvailability(
            @PathVariable Long id
    ) {

        doctorAvailabilityService.deleteAvailability(id);

        return ResponseEntity.ok(
                "Doctor availability deleted successfully."
        );
    }

    @PutMapping("/{doctorId}/emergency")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<DoctorAvailabilityResponse> markDoctorEmergency(
            @PathVariable Long doctorId) {

        return ResponseEntity.ok(
                doctorAvailabilityService.markDoctorEmergency(doctorId)
        );
    }

}