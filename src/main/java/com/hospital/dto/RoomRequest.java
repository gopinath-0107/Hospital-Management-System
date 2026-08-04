package com.hospital.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RoomRequest {

    @NotBlank(message = "Room Number is required")
    private String roomNumber;

    @NotNull(message = "Ward Id is required")
    private Long wardId;

    @NotNull(message = "Floor is required")
    private Integer floor;

    @NotNull(message = "Total Beds is required")
    @Min(value = 1, message = "Minimum 1 bed required")
    private Integer totalBeds;
}