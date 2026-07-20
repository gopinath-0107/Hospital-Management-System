package com.hospital.dto;

import com.hospital.enums.LabPriority;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class CreateLabOrderRequest {

    @NotNull(message = "Appointment ID is required.")
    @Positive(message = "Appointment ID must be greater than 0.")
    private Long appointmentId;

    @NotNull(message = "Lab Test ID is required.")
    @Positive(message = "Lab Test ID must be greater than 0.")
    private Long labTestId;

    private String clinicalNotes;

    private LabPriority priority;

    private String instructions;

}