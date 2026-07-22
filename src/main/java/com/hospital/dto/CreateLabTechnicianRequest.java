package com.hospital.dto;

import com.hospital.enums.Gender;
import com.hospital.enums.Shift;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CreateLabTechnicianRequest {

    @NotBlank(message = "First name is required")
    @Size(min = 2, max = 50,
            message = "First name must be between 2 and 50 characters")
    @Pattern(
            regexp = "^[A-Za-z ]+$",
            message = "First name must contain only letters"
    )
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(min = 2, max = 50,
            message = "Last name must be between 2 and 50 characters")
    @Pattern(
            regexp = "^[A-Za-z ]+$",
            message = "Last name must contain only letters"
    )
    private String lastName;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Size(max = 100,
            message = "Email must not exceed 100 characters")
    private String email;

    @NotBlank(message = "Mobile number is required")
    @Pattern(
            regexp = "^[6-9]\\d{9}$",
            message = "Mobile number must be 10 digits and start with 6-9"
    )
    private String mobile;

    @NotBlank(message = "Password is required")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&#])[A-Za-z\\d@$!%*?&#]{8,20}$",
            message = "Password must contain at least 8 characters, one uppercase, one lowercase, one number and one special character"
    )
    private String password;

    @NotNull(message = "Gender is required")
    private Gender gender;

    @NotBlank(message = "Qualification is required")
    @Size(min = 2, max = 100,
            message = "Qualification must be between 2 and 100 characters")
    private String qualification;

    @NotNull(message = "Experience is required")
    @Min(value = 0,
            message = "Experience cannot be negative")
    @Max(value = 50,
            message = "Experience cannot exceed 50 years")
    private Integer experience;

    @NotBlank(message = "Certificate number is required")
    @Size(min = 5, max = 50,
            message = "Certificate number must be between 5 and 50 characters")
    @Pattern(
            regexp = "^[A-Za-z0-9-]+$",
            message = "Certificate number can contain only letters, numbers and '-'"
    )
    private String certificateNumber;


    @NotNull(message = "Shift is required")
    private Shift shift;
}