package com.clp.controller;

import com.clp.dto.UserDto;
import com.clp.entity.User;
import com.clp.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// TODO Change based on writer and publisher

@RequestMapping("/users")
@RestController
@Tag(name = "User API")
@CrossOrigin(maxAge = 3600)
public class UserController {
    private final UserService userService;
    private final ModelMapper modelMapper;

    @Autowired
    public UserController(UserService userService, ModelMapper modelMapper) {
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("api/v1/get")
    public ResponseEntity<UserDto> authenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        User currentUser = (User) authentication.getPrincipal();

        return ResponseEntity.ok(modelMapper.map(currentUser, UserDto.class));
    }

    @GetMapping("api/v1/all")
    public ResponseEntity<List<UserDto>> allUsers() {
        List <User> users = userService.allUsers();

        return ResponseEntity.ok(modelMapper.map(users, new TypeToken<List<UserDto>>(){}.getType()));
    }
}
