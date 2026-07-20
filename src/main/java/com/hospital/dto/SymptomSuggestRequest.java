package com.hospital.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SymptomSuggestRequest {

    @NotEmpty(message = "At least one symptom must be selected")
    private List<String> symptomNames;
}
