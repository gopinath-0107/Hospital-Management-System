package com.hospital.controller;


import com.hospital.dto.CreateReceptionistRequest;
import com.hospital.dto.ReceptionistResponse;
import com.hospital.service.ReceptionistService;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.List;



@RestController
@RequestMapping("/api/receptionists")
@RequiredArgsConstructor
public class ReceptionistController {


    private final ReceptionistService receptionistService;




    @PostMapping
    public ResponseEntity<ReceptionistResponse> createReceptionist(
            @Valid @RequestBody CreateReceptionistRequest request) {


        return new ResponseEntity<>(

                receptionistService.createReceptionist(request),

                HttpStatus.CREATED
        );
    }




    @GetMapping("/{id}")
    public ResponseEntity<ReceptionistResponse> getReceptionistById(
            @PathVariable Long id) {


        return ResponseEntity.ok(

                receptionistService.getReceptionistById(id)
        );
    }





    @GetMapping
    public ResponseEntity<List<ReceptionistResponse>> getAllReceptionists() {


        return ResponseEntity.ok(

                receptionistService.getAllReceptionists()
        );
    }





    @PutMapping("/{id}")
    public ResponseEntity<ReceptionistResponse> updateReceptionist(

            @PathVariable Long id,

            @Valid @RequestBody CreateReceptionistRequest request) {


        return ResponseEntity.ok(

                receptionistService.updateReceptionist(id, request)
        );
    }





    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteReceptionist(

            @PathVariable Long id) {


        receptionistService.deleteReceptionist(id);


        return ResponseEntity.ok(
                "Receptionist deleted successfully"
        );
    }
}