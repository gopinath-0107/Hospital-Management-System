package com.hospital.dto.response;

import com.hospital.enums.Gender;
import com.hospital.enums.Role;
import com.hospital.enums.Status;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NurseResponse {

    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private String mobile;

    private Gender gender;

    private String qualification;

    private Integer experience;

    private String departmentName;

    private String shift;

    private Role role;

    private Status status;
}