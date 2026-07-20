package com.hospital.controller;


import com.hospital.dto.CreateNurseRequest;
import com.hospital.dto.NurseResponse;
import com.hospital.service.NurseService;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/nurses")
@RequiredArgsConstructor
public class NurseController {


    private final NurseService nurseService;



    @PostMapping
    public ResponseEntity<NurseResponse> createNurse(
            @Valid @RequestBody CreateNurseRequest request) {


        return new ResponseEntity<>(
                nurseService.createNurse(request),
                HttpStatus.CREATED
        );
    }



    @GetMapping("/{id}")
    public ResponseEntity<NurseResponse> getNurseById(
            @PathVariable Long id) {


        return ResponseEntity.ok(
                nurseService.getNurseById(id)
        );
    }



    @GetMapping
    public ResponseEntity<List<NurseResponse>> getAllNurses() {


        return ResponseEntity.ok(
                nurseService.getAllNurses()
        );
    }



    @PutMapping("/{id}")
    public ResponseEntity<NurseResponse> updateNurse(
            @PathVariable Long id,
            @Valid @RequestBody CreateNurseRequest request) {


        return ResponseEntity.ok(
                nurseService.updateNurse(id, request)
        );
    }



    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteNurse(
            @PathVariable Long id) {


        nurseService.deleteNurse(id);

        return ResponseEntity.ok(
                "Nurse deleted successfully"
        );
    }
}