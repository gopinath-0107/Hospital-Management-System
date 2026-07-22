package com.hospital.dto;

import com.hospital.enums.Gender;
import com.hospital.enums.Shift;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CreateReceptionistRequest {

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
            regexp = "^[6-9][0-9]{9}$",
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


    @NotNull(message = "Shift is required")
    private Shift shift;

}