package com.hospital.controller;


import com.hospital.dto.*;
import com.hospital.dto.request.PatientRegistrationRequest;
import com.hospital.dto.response.ApiResponse;
import com.hospital.dto.response.PatientResponse;
import com.hospital.service.AuthService;
import com.hospital.service.PaymentService;
import com.hospital.service.ReceptionistService;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;



@RestController
@RequestMapping("/api/receptionists")
@RequiredArgsConstructor
public class ReceptionistController {


    private final ReceptionistService receptionistService;

    private final PaymentService paymentService;

    private final AuthService authService;




    @PostMapping
    public ResponseEntity<ReceptionistResponse> createReceptionist(
            @Valid @RequestBody CreateReceptionistRequest request) {


        return new ResponseEntity<>(

                receptionistService.createReceptionist(request),

                HttpStatus.CREATED
        );
    }



    @PreAuthorize("hasRole('RECEPTIONIST')")
    @PostMapping("/register/patients")
    public ResponseEntity<ApiResponse<PatientResponse>> registerOfflinePatient(
            @Valid @RequestBody PatientRegistrationRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(authService.registerPatient(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReceptionistResponse> getReceptionistById(
            @PathVariable Long id) {


        return ResponseEntity.ok(

                receptionistService.getReceptionistById(id)
        );
    }





    @GetMapping
    public ResponseEntity<List<ReceptionistResponse>> getAllReceptionists() {


        return ResponseEntity.ok(

                receptionistService.getAllReceptionists()
        );
    }





    @PutMapping("/{id}")
    public ResponseEntity<ReceptionistResponse> updateReceptionist(

            @PathVariable Long id,

            @Valid @RequestBody CreateReceptionistRequest request) {


        return ResponseEntity.ok(

                receptionistService.updateReceptionist(id, request)
        );
    }





    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteReceptionist(

            @PathVariable Long id) {


        receptionistService.deleteReceptionist(id);


        return ResponseEntity.ok(
                "Receptionist deleted successfully"
        );
    }

    @PostMapping("/consultation-payment/{appointmentId}")
    public ResponseEntity<PaymentResponse> consultationPayment(
            @PathVariable Long appointmentId,
            @Valid @RequestBody PaymentRequest request) {

        return ResponseEntity.ok(
                paymentService.consultationPayment(
                        appointmentId,
                        request
                )
        );
    }

    @GetMapping("/consultation-receipt/{appointmentId}")
    @PreAuthorize("hasAnyRole('PATIENT','ADMIN','RECEPTIONIST')")
    public ResponseEntity<ReceiptResponse> consultationReceipt(
            @PathVariable Long appointmentId) {

        return ResponseEntity.ok(
                paymentService.getConsultationReceipt(
                        appointmentId
                )
        );
    }
}