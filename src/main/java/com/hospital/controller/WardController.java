package com.hospital.controller;

import com.hospital.dto.WardRequest;
import com.hospital.dto.WardResponse;
import com.hospital.service.WardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wards")
@RequiredArgsConstructor
public class WardController {

    private final WardService wardService;

    /**
     * Create Ward
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<WardResponse> createWard(
            @Valid @RequestBody WardRequest request) {

        WardResponse response = wardService.createWard(request);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Get All Wards
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','RECEPTIONIST')")
    public ResponseEntity<List<WardResponse>> getAllWards() {

        return ResponseEntity.ok(wardService.getAllWards());
    }

    /**
     * Get Ward By Id
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','RECEPTIONIST')")
    public ResponseEntity<WardResponse> getWardById(
            @PathVariable Long id) {

        return ResponseEntity.ok(wardService.getWardById(id));
    }

    /**
     * Update Ward
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<WardResponse> updateWard(
            @PathVariable Long id,
            @Valid @RequestBody WardRequest request) {

        return ResponseEntity.ok(wardService.updateWard(id, request));
    }

    /**
     * Delete Ward
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteWard(
            @PathVariable Long id) {

        wardService.deleteWard(id);

        return ResponseEntity.ok("Ward deleted successfully.");
    }

    /**
     * Get Active Wards
     */
    @GetMapping("/active")
    @PreAuthorize("hasAnyRole('ADMIN','RECEPTIONIST','DOCTOR')")
    public ResponseEntity<List<WardResponse>> getActiveWards() {

        return ResponseEntity.ok(wardService.getActiveWards());
    }

}