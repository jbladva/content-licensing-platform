package com.clp.service.impl;

import com.clp.entity.User;
import com.clp.repository.UserRepository;
import com.clp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> allUsers() {
        return userRepository.findAll();
    }
}
