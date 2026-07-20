package com.hospital.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PharmacistResponse {

    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private String mobile;

    private String qualification;

    private String licenseNumber;

}