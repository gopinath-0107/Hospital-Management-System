package com.hospital.dto;

import com.hospital.enums.LabOrderStatus;
import com.hospital.enums.LabPriority;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class LabOrderResponse {

    private Long id;
    private Long appointmentId;
    private Long labTestId;
    private String labTestName;
    private String patientName;
    private String clinicalNotes;

    private LabPriority priority;

    private String instructions;
    private LabOrderStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}