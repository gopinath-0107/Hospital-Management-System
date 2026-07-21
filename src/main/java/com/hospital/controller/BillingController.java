package com.hospital.controller;

import com.hospital.dto.*;
import com.hospital.service.BillingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/billing")
@RequiredArgsConstructor
public class BillingController {


    private final BillingService billingService;



    // Generate Bill

    @PostMapping
    public ResponseEntity<BillingResponse> generateBill(
            @RequestBody BillingRequest request) {


        BillingResponse response =
                billingService.generateBill(request);


        return new ResponseEntity<>(
                response,
                HttpStatus.CREATED
        );
    }




    // Get Bill By Id

    @GetMapping("/{id}")
    public ResponseEntity<BillingResponse> getBillById(
            @PathVariable Long id) {


        BillingResponse response =
                billingService.getBillById(id);


        return ResponseEntity.ok(response);
    }





    // Get All Bills

    @GetMapping
    public ResponseEntity<List<BillingResponse>> getAllBills() {


        return ResponseEntity.ok(
                billingService.getAllBills()
        );
    }





    // Make Payment

    @PostMapping("/{id}/payment")
    public ResponseEntity<PaymentResponse> makePayment(
            @PathVariable Long id,
            @Valid @RequestBody PaymentRequest request) {


        PaymentResponse response =
                billingService.makePayment(id, request);


        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/receipt")
    public ResponseEntity<ReceiptResponse> getReceipt(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                billingService.getReceipt(id)
        );
    }

}