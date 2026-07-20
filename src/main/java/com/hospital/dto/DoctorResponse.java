package com.hospital.dto;

import com.hospital.enums.Gender;
import com.hospital.enums.Role;
import com.hospital.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DoctorResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String mobile;
    private String departmentName;
    private String qualification;
    private Gender gender;
    private Integer experience;
    private String specialization;
    private BigDecimal consultationFee;
    private Boolean available;
    private String profileImage;
    private Role role;
    private Status status;
    private String licenseNumber;
}
