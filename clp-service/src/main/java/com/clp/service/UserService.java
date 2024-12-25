package com.clp.service;

import com.clp.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {

    List<User> allUsers();
}
