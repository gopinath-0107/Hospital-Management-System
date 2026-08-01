package com.hospital.dto;


import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;


@Data
public class ConsultationResponse {


    private Long id;


    private Long appointmentId;


    private Long doctorId;


    private Long patientId;




    private String bloodPressure;


    private Double temperature;


    private Integer pulseRate;


    private String diagnosis;


    private String notes;


    private LocalDateTime createdAt;

}