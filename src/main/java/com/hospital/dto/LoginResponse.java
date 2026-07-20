package com.hospital.dto;

import com.hospital.enums.Role;
import com.hospital.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    private Long id;
    private String token;
    private String firstName;
    private String lastName;
    private String email;
    private Role role;
    private Status status;
}
