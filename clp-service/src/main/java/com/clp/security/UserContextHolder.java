package com.clp.security;

import com.clp.entity.User;
import org.springframework.stereotype.Component;

public class UserContextHolder {

    private static final ThreadLocal<User> userThreadLocal = new ThreadLocal<>();

    public static User getUser() {
        return userThreadLocal.get();
    }

    public static void setUser(User user) {
        userThreadLocal.set(user);
    }
}
