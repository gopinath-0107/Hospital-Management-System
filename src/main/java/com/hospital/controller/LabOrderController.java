package com.hospital.controller;

import com.hospital.dto.CreateLabOrderRequest;
import com.hospital.dto.LabOrderResponse;
import com.hospital.enums.LabOrderStatus;
import com.hospital.service.LabOrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lab-orders")
@RequiredArgsConstructor
public class LabOrderController {

    private final LabOrderService labOrderService;

    @PostMapping
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<LabOrderResponse> createLabOrder(@Valid @RequestBody CreateLabOrderRequest request) {
        return new ResponseEntity<>(labOrderService.createLabOrder(request), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('DOCTOR', 'PATIENT', 'LAB_TECHNICIAN', 'ADMIN')")
    public ResponseEntity<LabOrderResponse> getLabOrderById(@PathVariable Long id) {
        return ResponseEntity.ok(labOrderService.getLabOrderById(id));
    }

    @GetMapping("/appointment/{appointmentId}")
    @PreAuthorize("hasAnyRole('DOCTOR', 'PATIENT', 'LAB_TECHNICIAN', 'ADMIN')")
    public ResponseEntity<List<LabOrderResponse>> getOrdersByAppointment(@PathVariable Long appointmentId) {
        return ResponseEntity.ok(labOrderService.getOrdersByAppointment(appointmentId));
    }

    @GetMapping("/doctor/{doctorId}")
    @PreAuthorize("hasAnyRole('DOCTOR', 'ADMIN')")
    public ResponseEntity<List<LabOrderResponse>> getOrdersByDoctor(@PathVariable Long doctorId) {
        return ResponseEntity.ok(labOrderService.getOrdersByDoctor(doctorId));
    }

    @GetMapping("/patient/{patientId}")
    @PreAuthorize("hasAnyRole('PATIENT', 'ADMIN')")
    public ResponseEntity<List<LabOrderResponse>> getOrdersByPatient(@PathVariable Long patientId) {
        return ResponseEntity.ok(labOrderService.getOrdersByPatient(patientId));
    }

    @GetMapping("/status")
    @PreAuthorize("hasAnyRole('LAB_TECHNICIAN', 'ADMIN')")
    public ResponseEntity<List<LabOrderResponse>> getOrdersByStatus(@RequestParam LabOrderStatus status) {
        return ResponseEntity.ok(labOrderService.getOrdersByStatus(status));
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('LAB_TECHNICIAN', 'ADMIN')")
    public ResponseEntity<LabOrderResponse> updateStatus(
            @PathVariable Long id,
            @RequestParam LabOrderStatus status) {
        return ResponseEntity.ok(labOrderService.updateStatus(id, status));
    }
}
