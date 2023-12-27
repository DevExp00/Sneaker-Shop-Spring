package com.marketplace.demo.services;

import com.marketplace.demo.entities.Users;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    Users getUserByEmail(String email);
    Users createUser(Users user);

    Users saveUser(Users user);

}
