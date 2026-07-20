package com.hospital.dto;

import com.hospital.enums.Gender;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PharmacistRequest {

    private String firstName;

    private String lastName;

    private String email;

    private String mobile;

    private String password;

    private Gender gender;

    private String qualification;

    private String licenseNumber;

}