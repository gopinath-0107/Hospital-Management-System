package com.hospital.dto;

import com.hospital.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SpecializationResponse {

    private Long id;

    private String specializationName;

    private Long departmentId;

    private String departmentName;

    private Status status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}