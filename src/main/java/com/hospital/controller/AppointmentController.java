package com.hospital.controller;

import com.hospital.dto.AppointmentRequest;
import com.hospital.dto.AppointmentResponse;
import com.hospital.enums.AppointmentStatus;
import com.hospital.service.AppointmentService;
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
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
@Tag(name = "Appointment Module", description = "Endpoints for booking and tracking appointments.")
public class AppointmentController {

    private final AppointmentService appointmentService;

    @PostMapping
    @PreAuthorize("hasRole('PATIENT')")
    @Operation(summary = "Book an Appointment for a Patient ")
    public ResponseEntity<AppointmentResponse> bookAppointment(@Valid @RequestBody AppointmentRequest request) {
        AppointmentResponse response = appointmentService.bookAppointment(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('RECEPTIONIST', 'DOCTOR', 'ADMIN')")
    @Operation(summary = "Update Appointment Status (RECEPTIONIST/DOCTOR/ADMIN)")
    public ResponseEntity<AppointmentResponse> updateAppointmentStatus(
            @PathVariable Long id,
            @RequestParam AppointmentStatus status) {
        AppointmentResponse response = appointmentService.updateAppointmentStatus(id, status);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('RECEPTIONIST', 'DOCTOR', 'PATIENT', 'ADMIN')")
    @Operation(summary = "Get Appointment details by ID")
    public ResponseEntity<AppointmentResponse> getAppointmentById(@PathVariable Long id) {
        AppointmentResponse response = appointmentService.getAppointmentById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/patient/{patientId}")
    @PreAuthorize("hasAnyRole('RECEPTIONIST', 'PATIENT', 'ADMIN')")
    @Operation(summary = "Get all Appointments of a specific Patient")
    public ResponseEntity<List<AppointmentResponse>> getAppointmentsByPatientId(@PathVariable Long patientId) {
        List<AppointmentResponse> response = appointmentService.getAppointmentsByPatientId(patientId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/doctor/{doctorId}")
    @PreAuthorize("hasAnyRole('RECEPTIONIST', 'DOCTOR', 'ADMIN')")
    @Operation(summary = "Get all Appointments of a specific Doctor")
    public ResponseEntity<List<AppointmentResponse>> getAppointmentsByDoctorId(@PathVariable Long doctorId) {
        List<AppointmentResponse> response = appointmentService.getAppointmentsByDoctorId(doctorId);
        return ResponseEntity.ok(response);
    }


    @PutMapping("/{id}/approve")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<AppointmentResponse> approveAppointment(@PathVariable Long id) {

        return ResponseEntity.ok(
                appointmentService.approveAppointment(id)
        );
    }


    @PutMapping("/{id}/reject")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<AppointmentResponse> rejectAppointment(@PathVariable Long id) {

        return ResponseEntity.ok(
                appointmentService.rejectAppointment(id)
        );
    }


    @PutMapping("/{id}/complete")
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR')")
    public ResponseEntity<AppointmentResponse> completeAppointment(@PathVariable Long id) {

        return ResponseEntity.ok(
                appointmentService.completeAppointment(id)
        );
    }

    @PutMapping("/{id}/consultation-completed")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<AppointmentResponse> consultationCompleted(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                appointmentService.consultationCompleted(id)
        );
    }

    @GetMapping("/doctor/{doctorId}/pending")
    @PreAuthorize("hasRole('DOCTOR')")
    @Operation(summary = "Get Pending Appointments for Doctor")
    public ResponseEntity<List<AppointmentResponse>> getPendingAppointmentsByDoctor(
            @PathVariable Long doctorId) {

        return ResponseEntity.ok(
                appointmentService.getPendingAppointmentsByDoctor(doctorId)
        );
    }


    @GetMapping("/doctor/{doctorId}/approved")
    @PreAuthorize("hasRole('DOCTOR')")
    @Operation(summary = "Get Approved Appointments for Doctor")
    public ResponseEntity<List<AppointmentResponse>> getApprovedAppointmentsByDoctor(
            @PathVariable Long doctorId) {

        return ResponseEntity.ok(
                appointmentService.getApprovedAppointmentsByDoctor(doctorId)
        );
    }


    @GetMapping("/doctor/{doctorId}/completed")
    @PreAuthorize("hasRole('DOCTOR')")
    @Operation(summary = "Get Completed Appointments for Doctor")
    public ResponseEntity<List<AppointmentResponse>> getCompletedAppointmentsByDoctor(
            @PathVariable Long doctorId) {

        return ResponseEntity.ok(
                appointmentService.getCompletedAppointmentsByDoctor(doctorId)
        );
    }
}
