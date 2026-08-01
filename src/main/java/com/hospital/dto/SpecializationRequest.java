package com.hospital.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SpecializationRequest {

    @NotBlank(message = "Specialization name is required")
    @Size(
            min = 3,
            max = 100,
            message = "Specialization name must be between 3 and 100 characters"
    )
    private String specializationName;

    @NotBlank(message = "Department code is required")
    private String departmentCode;

    @NotBlank(message = "Specialization code is required")
    private String specializationCode;



}