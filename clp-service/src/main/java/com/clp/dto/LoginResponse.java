package com.clp.dto;

import com.clp.enums.Role;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponse {

    private String token;
    private Role role;
    private Boolean isRegistered;
    private long expiresIn;
}
