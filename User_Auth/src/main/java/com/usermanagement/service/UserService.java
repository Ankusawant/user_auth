package com.usermanagement.service;

import com.usermanagement.entity.User;

import java.util.Optional;

public interface UserService {
    Optional<User> findByUsername(String username);
    void save(User user);
    Optional<User> findByEmail(String email);
}
