package com.hospital.controller;


import com.hospital.dto.*;
import com.hospital.service.LabTechnicianService;

import com.hospital.service.PaymentService;
import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.List;



@RestController
@RequestMapping("/api/lab-technicians")
@RequiredArgsConstructor
public class LabTechnicianController {



    private final LabTechnicianService labTechnicianService;

    private final PaymentService paymentService;



    @PostMapping
    public ResponseEntity<LabTechnicianResponse> createLabTechnician(

            @Valid @RequestBody CreateLabTechnicianRequest request) {


        return new ResponseEntity<>(

                labTechnicianService.createLabTechnician(request),

                HttpStatus.CREATED
        );
    }





    @GetMapping("/{id}")
    public ResponseEntity<LabTechnicianResponse> getLabTechnicianById(

            @PathVariable Long id) {


        return ResponseEntity.ok(

                labTechnicianService.getLabTechnicianById(id)
        );
    }





    @GetMapping
    public ResponseEntity<List<LabTechnicianResponse>> getAllLabTechnicians() {


        return ResponseEntity.ok(

                labTechnicianService.getAllLabTechnicians()
        );
    }





    @PutMapping("/{id}")
    public ResponseEntity<LabTechnicianResponse> updateLabTechnician(

            @PathVariable Long id,

            @Valid @RequestBody CreateLabTechnicianRequest request) {


        return ResponseEntity.ok(

                labTechnicianService.updateLabTechnician(id, request)
        );
    }





    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteLabTechnician(

            @PathVariable Long id) {


        labTechnicianService.deleteLabTechnician(id);


        return ResponseEntity.ok(
                "Lab Technician deleted successfully"
        );
    }

    @PostMapping("/payment/{labOrderId}")
    public ResponseEntity<PaymentResponse> laboratoryPayment(
            @PathVariable Long labOrderId,
            @Valid @RequestBody PaymentRequest request) {

        return ResponseEntity.ok(
                paymentService.laboratoryPayment(
                        labOrderId,
                        request
                )
        );
    }

    @GetMapping("/receipt/{labOrderId}")
    public ResponseEntity<ReceiptResponse> laboratoryReceipt(
            @PathVariable Long labOrderId) {

        return ResponseEntity.ok(
                paymentService.getLaboratoryReceipt(
                        labOrderId
                )
        );
    }

}