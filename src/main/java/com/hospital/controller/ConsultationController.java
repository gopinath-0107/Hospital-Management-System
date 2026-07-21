package com.hospital.controller;

import com.hospital.dto.CreateConsultationRequest;
import com.hospital.service.ConsultationService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/consultations")
@RequiredArgsConstructor
public class ConsultationController {


    private final ConsultationService consultationService;



    @PostMapping
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<?> create(
            @Valid @RequestBody CreateConsultationRequest request){

        return ResponseEntity.ok(
                consultationService.createConsultation(request)
        );

    }



    @GetMapping("/appointment/{appointmentId}")
    public ResponseEntity<?> getByAppointment(
            @PathVariable Long appointmentId){

        return ResponseEntity.ok(
                consultationService
                        .getByAppointmentId(appointmentId)
        );

    }


}