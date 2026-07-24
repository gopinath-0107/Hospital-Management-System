package com.hospital.controller;

import com.hospital.dto.CreateLabTestRequest;
import com.hospital.dto.LabTestResponse;
import com.hospital.service.LabTestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lab-tests")
@RequiredArgsConstructor
public class LabTestController {

    private final LabTestService labTestService;

    @PostMapping
    @PreAuthorize("hasRole('LAB_TECHNICIAN')")
    public ResponseEntity<LabTestResponse> createLabTest(@Valid @RequestBody CreateLabTestRequest request) {
        return new ResponseEntity<>(labTestService.createLabTest(request), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LabTestResponse> getLabTestById(@PathVariable Long id) {
        return ResponseEntity.ok(labTestService.getLabTestById(id));
    }

    @GetMapping
    public ResponseEntity<List<LabTestResponse>> getAllLabTests() {
        return ResponseEntity.ok(labTestService.getAllLabTests());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<LabTestResponse> updateLabTest(
            @PathVariable Long id,
            @Valid @RequestBody CreateLabTestRequest request) {
        return ResponseEntity.ok(labTestService.updateLabTest(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteLabTest(@PathVariable Long id) {
        labTestService.deleteLabTest(id);
        return ResponseEntity.ok("Lab Test deleted successfully.");
    }
}
