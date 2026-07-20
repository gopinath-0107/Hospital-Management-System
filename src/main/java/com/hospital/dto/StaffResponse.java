package com.hospital.dto;


import com.hospital.enums.Gender;
import com.hospital.enums.Status;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StaffResponse {

    private Long id;

    private String staffName;

    private String email;

    private String mobileNo;

    private Gender gender;

    private String designation;

    private String department;



    private BigDecimal salary;

    private String address;

    private Status status;

    private LocalDateTime createdAt;
}