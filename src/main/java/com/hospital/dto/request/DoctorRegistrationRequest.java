package com.hospital.dto.request;

import com.hospital.enums.Gender;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class DoctorRegistrationRequest {

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

    @NotNull(message = "Department is required")
    @Positive(message = "Department Id must be greater than 0")
    private Long departmentId;

    @NotBlank(message = "Qualification is required")
    @Size(min = 2, max = 100,
            message = "Qualification must be between 2 and 100 characters")
    private String qualification;

    @NotNull(message = "Experience is required")
    @Min(value = 0,
            message = "Experience cannot be negative")
    @Max(value = 60,
            message = "Experience cannot be greater than 60 years")
    private Integer experience;

    @NotNull(message = "Specialization ID is required")
    @Positive(message = "Specialization ID must be greater than 0")
    private Long specializationId;

    @NotNull(message = "Consultation fee is required")
    @DecimalMin(
            value = "0.0",
            inclusive = false,
            message = "Consultation fee must be greater than 0"
    )
    @Digits(
            integer = 8,
            fraction = 2,
            message = "Invalid consultation fee"
    )
    private BigDecimal consultationFee;

    @NotBlank(message = "License number is required")
    @Size(min = 5, max = 30,
            message = "License number must be between 5 and 30 characters")
    @Pattern(
            regexp = "^[A-Za-z0-9-]+$",
            message = "License number can contain only letters, numbers and '-'"
    )
    private String licenseNumber;
}