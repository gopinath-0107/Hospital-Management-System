package com.hospital.dto;


import lombok.Data;

import java.util.List;


@Data
public class CreateConsultationRequest {


    private Long appointmentId;


    private List<Long> symptomIds;


    private String bloodPressure;


    private Double temperature;


    private Integer pulseRate;


    private String diagnosis;


    private String notes;

}