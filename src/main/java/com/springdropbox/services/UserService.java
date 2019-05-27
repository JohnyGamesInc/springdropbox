package com.springdropbox.services;

import com.springdropbox.entities.SystemUser;
import com.springdropbox.entities.User;

import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    User findByUserName(String username);

    boolean save(SystemUser systemUser);
}
