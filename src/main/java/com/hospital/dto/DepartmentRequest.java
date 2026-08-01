package com.hospital.dto;

import com.hospital.enums.Status;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentRequest {

    @NotBlank(message = "Department name is required")
    @Size(
            min = 2,
            max = 100,
            message = "Department name must be between 2 and 100 characters"
    )
    @Pattern(
            regexp = "^[A-Za-z&()/\\- ]+$",
            message = "Department name can contain only letters, spaces, &, /, -, (, )"
    )
    private String departmentName;

    @Size(
            max = 500,
            message = "Description cannot exceed 500 characters"
    )
    private String description;


    @Min(value = 0, message = "Floor number cannot be negative")
    @Max(value = 100, message = "Floor number cannot be greater than 100")
    private Integer floorNumber;

    @NotBlank(message = "Department code is required")
    @Size(min = 2, max = 10, message = "Department code must be between 2 and 10 characters")
    @Pattern(
            regexp = "^[A-Z0-9]+$",
            message = "Department code must contain only uppercase letters and numbers"
    )
    private String departmentCode;

    @NotNull(message = "Status is required")
    private Status status;
}