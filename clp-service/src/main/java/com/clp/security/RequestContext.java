package com.clp.security;

import com.clp.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RequestContext {

    String userName;
    List<String> roles;
    String jwtHeader;
    String sessionId;
}
