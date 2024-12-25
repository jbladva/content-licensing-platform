package com.clp.dto;

import com.clp.enums.Role;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegisterUserDto {
    private String email;
    private String username;
    private String password;
    private Role role;
}
