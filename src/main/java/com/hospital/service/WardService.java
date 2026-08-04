package com.hospital.service;

import com.hospital.dto.WardRequest;
import com.hospital.dto.WardResponse;

import java.util.List;

public interface WardService {

    /**
     * Create a new ward
     */
    WardResponse createWard(WardRequest request);

    /**
     * Update an existing ward
     */
    WardResponse updateWard(Long id, WardRequest request);

    /**
     * Get ward by ID
     */
    WardResponse getWardById(Long id);

    /**
     * Get all wards
     */
    List<WardResponse> getAllWards();

    /**
     * Get all active wards
     */
    List<WardResponse> getActiveWards();

    /**
     * Delete ward
     */
    void deleteWard(Long id);

}