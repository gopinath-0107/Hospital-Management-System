package com.hospital.controller;

import com.hospital.dto.PharmacistRequest;
import com.hospital.dto.PharmacistResponse;
import com.hospital.service.PharmacistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pharmacists")
@RequiredArgsConstructor
public class PharmacistController {

    private final PharmacistService pharmacistService;


    // Create Pharmacist
    @PostMapping
    public ResponseEntity<PharmacistResponse> createPharmacist(
            @RequestBody PharmacistRequest request) {

        PharmacistResponse response =
                pharmacistService.createPharmacist(request);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


    // Get Pharmacist By Id
    @GetMapping("/{id}")
    public ResponseEntity<PharmacistResponse> getPharmacistById(
            @PathVariable Long id) {

        PharmacistResponse response =
                pharmacistService.getPharmacistById(id);

        return ResponseEntity.ok(response);
    }


    // Get All Pharmacists
    @GetMapping
    public ResponseEntity<List<PharmacistResponse>> getAllPharmacists() {

        return ResponseEntity.ok(
                pharmacistService.getAllPharmacists()
        );
    }


    // Update Pharmacist
    @PutMapping("/{id}")
    public ResponseEntity<PharmacistResponse> updatePharmacist(
            @PathVariable Long id,
            @RequestBody PharmacistRequest request) {

        PharmacistResponse response =
                pharmacistService.updatePharmacist(id, request);

        return ResponseEntity.ok(response);
    }


    // Delete Pharmacist
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePharmacist(
            @PathVariable Long id) {

        pharmacistService.deletePharmacist(id);

        return ResponseEntity.ok("Pharmacist deleted successfully.");
    }

}