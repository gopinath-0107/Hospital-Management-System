package com.hospital.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SymptomRequest {

    @NotBlank(message = "Symptom name is required")
    private String symptomName;

    @NotNull(message = "Department ID is required")
    private Long departmentId;
}
