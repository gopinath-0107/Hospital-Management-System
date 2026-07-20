package com.hospital.dto.response;

import com.hospital.enums.Gender;
import com.hospital.enums.Role;
import com.hospital.enums.Status;
import lombok.*;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PatientResponse {

    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private String mobile;

    private Gender gender;

    private LocalDate dateOfBirth;

    private String bloodGroup;

    private Double height;

    private Double weight;

    private String address;

    private String city;

    private String state;

    private String pincode;

    private String emergencyContact;

    private Role role;

    private Status status;
}