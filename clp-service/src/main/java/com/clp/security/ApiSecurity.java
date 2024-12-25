package com.clp.security;

import com.clp.enums.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ApiSecurity {

    private final com.clp.security.RequestContext requestContext;

    @Autowired
    public ApiSecurity(RequestContext requestContext) {
        this.requestContext = requestContext;
    }

    public boolean hasUserRole(){
        return requestContext.getRoles().stream()
                .anyMatch(s -> s.contains(Role.USER.toString()));
    }

    public boolean hasWriterRole(){
        return requestContext.getRoles().stream()
                .anyMatch(s -> s.contains(Role.WRITER.toString()));
    }

    public boolean hasPublisherRole(){
        return requestContext.getRoles().stream()
                .anyMatch(s -> s.contains(Role.PUBLISHER.toString()));
    }
}
