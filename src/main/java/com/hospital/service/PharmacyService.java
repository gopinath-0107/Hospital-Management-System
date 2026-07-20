package com.hospital.service;

import com.hospital.dto.PrescriptionResponse;

import java.util.List;

public interface PharmacyService {

    void dispenseMedicine(Long prescriptionId, Long pharmacistId);

    List<PrescriptionResponse> getPendingPrescriptions();

}