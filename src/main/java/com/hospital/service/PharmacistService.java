package com.hospital.service;

import com.hospital.dto.PharmacistRequest;
import com.hospital.dto.PharmacistResponse;

import java.util.List;

public interface PharmacistService {

    // Create Pharmacist
    PharmacistResponse createPharmacist(PharmacistRequest request);

    // Update Pharmacist
    PharmacistResponse updatePharmacist(Long id, PharmacistRequest request);

    // Get Pharmacist By Id
    PharmacistResponse getPharmacistById(Long id);

    // Get All Pharmacists
    List<PharmacistResponse> getAllPharmacists();

    // Delete Pharmacist
    void deletePharmacist(Long id);

}