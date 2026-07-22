package com.hospital.controller;

import com.hospital.dto.DoctorResponse;
import com.hospital.dto.request.DoctorRegistrationRequest;
import com.hospital.dto.request.LoginRequest;
import com.hospital.dto.request.PatientRegistrationRequest;
import com.hospital.dto.response.*;
import com.hospital.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Authentication and Registration APIs")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    @Operation(summary = "Login to the system", description = "Returns JWT Token upon successful authentication")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/register/doctor")
    public ResponseEntity<ApiResponse<DoctorResponse>> registerDoctor(
            @Valid @RequestBody DoctorRegistrationRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(authService.registerDoctor(request));
    }


    @PostMapping("/register/patient")
    @Operation(
            summary = "Register Patient",
            description = "Patient self-registration"
    )
    public ResponseEntity<ApiResponse<PatientResponse>> registerPatient(
            @Valid @RequestBody PatientRegistrationRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(authService.registerPatient(request));
    }



}
