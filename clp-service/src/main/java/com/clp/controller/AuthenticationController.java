package com.clp.controller;

import com.clp.dto.LoginResponse;
import com.clp.dto.LoginUserDto;
import com.clp.dto.RegisterUserDto;
import com.clp.dto.UserDto;
import com.clp.entity.User;
import com.clp.service.AuthenticationService;
import com.clp.service.JwtService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("api")
@RestController
@Tag(name = "Auth API")
@CrossOrigin(maxAge = 3600)
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final ModelMapper modelMapper;

    public AuthenticationController(AuthenticationService authenticationService,
                                    ModelMapper modelMapper) {
        this.authenticationService = authenticationService;
        this.modelMapper = modelMapper;
    }

    @PostMapping("v1/auth/signup")
    public ResponseEntity<UserDto> register(@RequestBody RegisterUserDto registerUserDto) {
        User registeredUser = authenticationService.signup(registerUserDto);
        return ResponseEntity.ok(modelMapper.map(registeredUser, UserDto.class));
    }

    @PostMapping("v1/auth/login")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody LoginUserDto loginUserDto) {
        return ResponseEntity.ok(authenticationService.authenticate(loginUserDto));
    }
}
