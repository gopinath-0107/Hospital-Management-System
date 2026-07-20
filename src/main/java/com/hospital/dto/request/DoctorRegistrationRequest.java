package com.hospital.dto.request;

import com.hospital.enums.Gender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class DoctorRegistrationRequest {

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Mobile number is required")
    @Pattern(regexp = "^[6-9]\\d{9}$", message = "Mobile number must be 10 digits and start with 6-9")
    private String mobile;

    @NotBlank(message = "Password is required")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&#])[A-Za-z\\d@$!%*?&#]{8,}$", 
             message = "Password must be at least 8 characters, contain uppercase, lowercase, number and special character")
    private String password;

    @NotNull(message = "Gender is required")
    private Gender gender;

    private Long departmentId;

    @NotBlank(message = "Qualification is required")
    private String qualification;

    private Integer experience;

    @NotBlank(message = "Specialization is required")
    private String specialization;

    private java.math.BigDecimal consultationFee;

    @NotBlank(message = "License number is required")
    private String licenseNumber;
}
