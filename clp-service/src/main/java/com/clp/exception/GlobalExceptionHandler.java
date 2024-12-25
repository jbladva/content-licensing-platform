package com.clp.exception;

import com.clp.Views;
import com.clp.enums.Role;
import com.clp.security.UserContextHolder;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.AbstractMappingJacksonResponseBodyAdvice;

import java.nio.file.AccessDeniedException;
import java.security.SignatureException;
import java.util.List;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ProblemDetail handleSecurityException(Exception exception) {
        ProblemDetail errorDetail = null;

        log.error(exception.getMessage(), exception);

        if (exception instanceof BadCredentialsException) {
            errorDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(401), exception.getMessage());
            errorDetail.setProperty("description", "The username or password is incorrect");

            return errorDetail;
        }

        if (exception instanceof ResponseStatusException) {
            errorDetail = ProblemDetail.forStatus(((ResponseStatusException) exception).getStatusCode());
            errorDetail.setDetail(((ResponseStatusException) exception).getReason());
            return errorDetail;
        }

        if (exception instanceof AccountStatusException) {
            errorDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(403), exception.getMessage());
            errorDetail.setProperty("description", "The account is locked");
        }

        if (exception instanceof AccessDeniedException) {
            errorDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(403), exception.getMessage());
            errorDetail.setProperty("description", "You are not authorized to access this resource");
        }

        if (exception instanceof SignatureException) {
            errorDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(403), exception.getMessage());
            errorDetail.setProperty("description", "The JWT signature is invalid");
        }

        if (exception instanceof ExpiredJwtException) {
            errorDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(403), exception.getMessage());
            errorDetail.setProperty("description", "The JWT token has expired");
        }

        if (errorDetail == null) {
            errorDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(500), exception.getMessage());
            errorDetail.setProperty("description", "Unknown internal server error.");
        }
        return errorDetail;
    }

//    @Override
//    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
//        return super.supports(returnType, converterType);
//    }


//    @Override
//    protected void beforeBodyWriteInternal(MappingJacksonValue bodyContainer, MediaType contentType,
//                                           MethodParameter returnType, ServerHttpRequest request, ServerHttpResponse response) {
//
//        Class<?> viewClass = bodyContainer.getSerializationView();
//
//        if (UserContextHolder.getUser()!= null && UserContextHolder.getUser().getRole() != null) {
//
//            Role role = UserContextHolder.getUser().getRole();
//            if (role.equals(Role.WRITER)) {
//                viewClass = Views.WriterView.class;
//            }
//            if (role.equals(Role.PUBLISHER)) {
//                viewClass = Views.PublisherView.class;
//            }
//        }
//        bodyContainer.setSerializationView(viewClass);
//
//    }
}

