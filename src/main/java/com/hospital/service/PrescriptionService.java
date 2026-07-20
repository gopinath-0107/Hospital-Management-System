package com.hospital.service;

import com.hospital.dto.PrescriptionRequest;
import com.hospital.dto.PrescriptionResponse;
import com.hospital.enums.PrescriptionStatus;

import java.util.List;

public interface PrescriptionService {


    PrescriptionResponse createPrescription(PrescriptionRequest request);


    PrescriptionResponse getPrescriptionById(Long id);


    PrescriptionResponse getPrescriptionByAppointment(Long appointmentId);


    List<PrescriptionResponse> getAllPrescriptions();

    void updatePrescriptionStatus(
            Long prescriptionId,
            PrescriptionStatus status
    );

}