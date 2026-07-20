package com.hospital.dto;

import com.hospital.enums.Gender;
import com.hospital.enums.Shift;

import jakarta.validation.constraints.*;
import lombok.Data;


@Data
public class CreateReceptionistRequest {


    @NotBlank(message = "First name is required")
    private String firstName;


    @NotBlank(message = "Last name is required")
    private String lastName;


    @Email(message = "Invalid email")
    @NotBlank(message = "Email is required")
    private String email;


    @NotBlank(message = "Mobile is required")
    @Pattern(
            regexp = "^[0-9]{10}$",
            message = "Mobile must be 10 digits"
    )
    private String mobile;


    @NotBlank(message = "Password is required")
    private String password;


    @NotNull(message = "Gender is required")
    private Gender gender;


    @NotNull(message = "Department is required")
    private Long departmentId;


    @NotNull(message = "Shift is required")
    private Shift shift;
}