package com.hospital.controller;

import com.hospital.dto.DepartmentSuggestResponse;
import com.hospital.dto.SymptomRequest;
import com.hospital.dto.SymptomResponse;
import com.hospital.dto.SymptomSuggestRequest;
import com.hospital.service.SymptomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/symptoms")
@RequiredArgsConstructor
@Tag(name = "Symptoms Module", description = "Endpoints for defining symptoms and suggesting departments/doctors.")
public class SymptomController {

    private final SymptomService symptomService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create a new Symptom Master record (ADMIN only)")
    public ResponseEntity<SymptomResponse> createSymptom(@Valid @RequestBody SymptomRequest request) {
        SymptomResponse response = symptomService.createSymptom(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/suggest")
    @PreAuthorize("hasRole('PATIENT')")
    @Operation(summary = "Suggest appropriate Doctor")
    public ResponseEntity<DepartmentSuggestResponse> suggestDepartmentAndDoctors(
            @Valid @RequestBody SymptomSuggestRequest request) {
        DepartmentSuggestResponse response = symptomService.suggestDepartmentAndDoctors(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(summary = "Retrieve all Symptoms (Any authenticated user)")
    public ResponseEntity<List<SymptomResponse>> getAllSymptoms() {
        List<SymptomResponse> response = symptomService.getAllSymptoms();
        return ResponseEntity.ok(response);
    }
}
