package com.clp.service;

import com.clp.dto.LoginResponse;
import com.clp.dto.LoginUserDto;
import com.clp.dto.RegisterUserDto;
import com.clp.entity.User;
import com.clp.repository.PublisherRepository;
import com.clp.repository.UserRepository;
import com.clp.repository.WriterRepository;
import com.clp.security.UserContextHolder;
import com.clp.util.Common;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final WriterRepository writerRepository;
    private final PublisherRepository publisherRepository;

    @Autowired
    public AuthenticationService(
            UserRepository userRepository,
            AuthenticationManager authenticationManager,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            WriterRepository writerRepository,
            PublisherRepository publisherRepository
    ) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.writerRepository = writerRepository;
        this.publisherRepository = publisherRepository;
    }

    public User signup(RegisterUserDto input) {
        validateUser(input);
        return userRepository.save(User.builder()
                .username(input.getUsername())
                .email(input.getEmail())
                .role(input.getRole())
                .password(passwordEncoder.encode(input.getPassword())).build());
    }

    public LoginResponse authenticate(LoginUserDto input) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.getUsername(),
                        input.getPassword()
                )
        );

        User authenticatedUser = userRepository.findByUsername(input.getUsername())
                .orElseThrow();
        String jwtToken = jwtService.generateToken(authenticatedUser);
        return LoginResponse.builder()
                .token(jwtToken)
                .isRegistered(isRegistered(authenticatedUser))
                .role(authenticatedUser.getRole())
                .expiresIn(jwtService.getExpirationTime()).build();
    }

    private void validateUser(RegisterUserDto userDto) {
        if (Boolean.FALSE.equals(Common.isValidEmail(userDto.getEmail())))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Please enter a valid email address");

        if (Boolean.FALSE.equals(Common.isValidPassword(userDto.getPassword())))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Please enter a valid password");

        userRepository.findByUsername(userDto.getUsername())
                .ifPresent(user -> {
                    throw new ResponseStatusException(HttpStatus.CONFLICT, "User is already exist");
                });
    }

    private boolean isRegistered(User user) {
        return switch (user.getRole()) {
            case WRITER -> writerRepository.existsByUser_Username(user.getUsername());
            case PUBLISHER -> publisherRepository.existsByUser_Username(user.getUsername());
            case USER -> true;
            default -> throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid role");
        };
    }
}
