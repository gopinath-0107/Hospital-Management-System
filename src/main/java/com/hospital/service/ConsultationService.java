package com.hospital.service;


import com.hospital.dto.*;


public interface ConsultationService {


    ConsultationResponse createConsultation(
            CreateConsultationRequest request
    );


    ConsultationResponse getByAppointmentId(
            Long appointmentId
    );

}