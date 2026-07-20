package com.hospital.dto.response;

import com.hospital.enums.Role;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthResponse {
    private String token;
    private Role role;
    private Long userId;
    private String name;
    private String email;
}
