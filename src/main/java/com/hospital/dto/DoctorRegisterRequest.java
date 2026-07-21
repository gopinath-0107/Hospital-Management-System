package com.hospital.dto;

import com.hospital.enums.Gender;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DoctorRegisterRequest {

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
            message = "Email cannot exceed 100 characters")
    private String email;


    @NotBlank(message = "Mobile number is required")
    @Pattern(
            regexp = "^[6-9]\\d{9}$",
            message = "Mobile number must be 10 digits and start with 6-9"
    )
    private String mobile;


    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 20,
            message = "Password must be between 8 and 20 characters")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&#]).{8,20}$",
            message = "Password must contain uppercase, lowercase, number and special character"
    )
    private String password;


    @NotNull(message = "Gender is required")
    private Gender gender;


    @NotNull(message = "Department ID is required")
    @Positive(message = "Department ID must be greater than 0")
    private Long departmentId;


    @NotBlank(message = "Qualification is required")
    @Size(min = 2, max = 100,
            message = "Qualification must be between 2 and 100 characters")
    private String qualification;


    @NotNull(message = "Experience is required")
    @Min(value = 0,
            message = "Experience cannot be negative")
    @Max(value = 60,
            message = "Experience cannot exceed 60 years")
    private Integer experience;


    @NotBlank(message = "Specialization is required")
    @Size(min = 2, max = 100,
            message = "Specialization must be between 2 and 100 characters")
    private String specialization;


    @NotNull(message = "Consultation fee is required")
    @DecimalMin(
            value = "1.00",
            message = "Consultation fee must be greater than 0"
    )
    @Digits(
            integer = 8,
            fraction = 2,
            message = "Invalid consultation fee"
    )
    private BigDecimal consultationFee;


    @NotBlank(message = "License number is required")
    @Size(min = 5, max = 50,
            message = "License number must be between 5 and 50 characters")
    private String licenseNumber;


    @Size(max = 255,
            message = "Profile image URL cannot exceed 255 characters")
    private String profileImage;
}