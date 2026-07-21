package com.hospital.dto;

import com.hospital.enums.Gender;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatientRegisterRequest {

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


    @NotNull(message = "Date of birth is required")
    @Past(message = "Date of birth must be in the past")
    private LocalDate dateOfBirth;


    @NotBlank(message = "Blood group is required")
    @Pattern(
            regexp = "^(A|B|AB|O)[+-]$",
            message = "Invalid blood group"
    )
    private String bloodGroup;


    @NotNull(message = "Height is required")
    @DecimalMin(
            value = "30.0",
            message = "Height must be at least 30 cm"
    )
    @DecimalMax(
            value = "300.0",
            message = "Height cannot exceed 300 cm"
    )
    private Double height;


    @NotNull(message = "Weight is required")
    @DecimalMin(
            value = "1.0",
            message = "Weight must be greater than 0"
    )
    @DecimalMax(
            value = "500.0",
            message = "Weight cannot exceed 500 kg"
    )
    private Double weight;


    @NotBlank(message = "Address is required")
    @Size(max = 255,
            message = "Address cannot exceed 255 characters")
    private String address;


    @NotBlank(message = "City is required")
    @Size(min = 2, max = 50,
            message = "City must be between 2 and 50 characters")
    @Pattern(
            regexp = "^[A-Za-z ]+$",
            message = "City must contain only letters"
    )
    private String city;


    @NotBlank(message = "State is required")
    @Size(min = 2, max = 50,
            message = "State must be between 2 and 50 characters")
    @Pattern(
            regexp = "^[A-Za-z ]+$",
            message = "State must contain only letters"
    )
    private String state;


    @NotBlank(message = "Pincode is required")
    @Pattern(
            regexp = "^\\d{6}$",
            message = "Pincode must be 6 digits"
    )
    private String pincode;


    @NotBlank(message = "Emergency contact is required")
    @Pattern(
            regexp = "^[6-9]\\d{9}$",
            message = "Emergency contact must be 10 digits and start with 6-9"
    )
    private String emergencyContact;
}