package com.hospital.dto;

import com.hospital.enums.WardStatus;
import com.hospital.enums.WardType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WardRequest {

    @NotBlank(message = "Ward name is required")
    @Size(max = 100, message = "Ward name cannot exceed 100 characters")
    private String wardName;

    @NotNull(message = "Ward type is required")
    private WardType wardType;

    @NotNull(message = "Floor is required")
    @Min(value = 0, message = "Floor cannot be negative")
    private Integer floor;

    @NotNull(message = "Total rooms is required")
    @Min(value = 1, message = "Total rooms must be at least 1")
    private Integer totalRooms;

    @Size(max = 255, message = "Description cannot exceed 255 characters")
    private String description;

    @NotNull(message = "Status is required")
    private WardStatus status;
}