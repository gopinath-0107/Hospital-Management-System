package com.hospital.controller;

import com.hospital.dto.PaymentRequest;
import com.hospital.dto.PaymentResponse;
import com.hospital.dto.PrescriptionResponse;
import com.hospital.dto.ReceiptResponse;
import com.hospital.security.CustomUserDetails;
import com.hospital.service.PaymentService;
import com.hospital.service.PharmacyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pharmacy")
@RequiredArgsConstructor
public class PharmacyController {

    private final PharmacyService pharmacyService;
    private final PaymentService paymentService;

    @PostMapping("/dispense/{prescriptionId}")
    @PreAuthorize("hasRole('PHARMACIST')")
    public ResponseEntity<String> dispenseMedicine(
            @PathVariable Long prescriptionId,
            @RequestParam Long pharmacistId) {

        pharmacyService.dispenseMedicine(prescriptionId, pharmacistId);

        return ResponseEntity.ok("Medicine dispensed successfully.");
    }


    @GetMapping("/pending-prescriptions")
    @PreAuthorize("hasRole('PHARMACIST')")
    public ResponseEntity<List<PrescriptionResponse>> getPendingPrescriptions() {

        return ResponseEntity.ok(pharmacyService.getPendingPrescriptions());

    }


    @PostMapping("/payment/{prescriptionId}")
    public ResponseEntity<PaymentResponse> pharmacyPayment(
            @PathVariable Long prescriptionId,
            @Valid @RequestBody PaymentRequest request) {

        return ResponseEntity.ok(
                paymentService.pharmacyPayment(
                        prescriptionId,
                        request
                )
        );
    }

    @GetMapping("/receipt/{prescriptionId}")
    @PreAuthorize("hasAnyRole('PATIENT','ADMIN','RECEPTIONIST')")
    public ResponseEntity<ReceiptResponse> pharmacyReceipt(
            @PathVariable Long prescriptionId) {

        return ResponseEntity.ok(
                paymentService.getPharmacyReceipt(
                        prescriptionId
                )
        );
    }
}