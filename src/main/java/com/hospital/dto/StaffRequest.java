package com.hospital.dto;

import com.hospital.enums.Gender;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StaffRequest {

    @NotBlank(message = "Staff name is required")
    private String staffName;


    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;


    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be minimum 6 characters")
    private String password;


    @NotBlank(message = "Mobile number is required")
    @Pattern(
            regexp = "^[0-9]{10}$",
            message = "Mobile number must be 10 digits"
    )
    private String mobileNo;


    @NotNull(message = "Gender is required")
    private Gender gender;


    @NotBlank(message = "Designation is required")
    private String designation;


    @NotBlank(message = "Department is required")
    private String department;



    @NotNull(message = "Salary is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Salary must be greater than zero")
    private BigDecimal salary;


    @NotBlank(message = "Address is required")
    private String address;
}