package com.hospital.controller;

import com.hospital.dto.SpecializationRequest;
import com.hospital.dto.SpecializationResponse;
import com.hospital.service.SpecializationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/specializations")
@RequiredArgsConstructor
public class SpecializationController {

    private final SpecializationService specializationService;



    @PostMapping("/import")
    public ResponseEntity<String> importSpecializations(
            @RequestParam MultipartFile file){

        specializationService.importSpecializations(file);

        return ResponseEntity.ok("Imported Successfully");
    }

    /*
     * Create Specialization
     * Only ADMIN
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SpecializationResponse> createSpecialization(
            @Valid @RequestBody SpecializationRequest request
    ) {

        return new ResponseEntity<>(
                specializationService.createSpecialization(request),
                HttpStatus.CREATED
        );
    }

    /*
     * Update Specialization
     * Only ADMIN
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SpecializationResponse> updateSpecialization(
            @PathVariable Long id,
            @Valid @RequestBody SpecializationRequest request
    ) {

        return ResponseEntity.ok(
                specializationService.updateSpecialization(id, request)
        );
    }

    /*
     * Soft Delete Specialization
     * Only ADMIN
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteSpecialization(
            @PathVariable Long id
    ) {

        specializationService.deleteSpecialization(id);

        return ResponseEntity.ok(
                "Specialization deleted successfully."
        );
    }

    /*
     * Get Specialization By Id
     * ADMIN / RECEPTIONIST / PATIENT
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','RECEPTIONIST','PATIENT')")
    public ResponseEntity<SpecializationResponse> getSpecializationById(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                specializationService.getSpecializationById(id)
        );
    }

    /*
     * Get All Active Specializations
     * ADMIN / RECEPTIONIST / PATIENT
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','RECEPTIONIST','PATIENT')")
    public ResponseEntity<List<SpecializationResponse>> getAllSpecializations() {

        return ResponseEntity.ok(
                specializationService.getAllSpecializations()
        );
    }

    /*
     * Get Specializations By Department
     * ADMIN / RECEPTIONIST / PATIENT
     */
    @GetMapping("/department/{departmentId}")
    @PreAuthorize("hasAnyRole('ADMIN','RECEPTIONIST','PATIENT')")
    public ResponseEntity<List<SpecializationResponse>> getByDepartment(
            @PathVariable Long departmentId
    ) {

        return ResponseEntity.ok(
                specializationService.getSpecializationsByDepartment(departmentId)
        );
    }

}