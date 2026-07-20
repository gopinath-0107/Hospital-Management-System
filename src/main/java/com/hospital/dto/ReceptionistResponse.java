package com.hospital.dto;

import com.hospital.enums.Gender;
import com.hospital.enums.Shift;
import com.hospital.enums.Status;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;


@Data
@Builder
public class ReceptionistResponse {


    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private String mobile;

    private Gender gender;

    private Long departmentId;

    private String departmentName;

    private Shift shift;

    private Status status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}