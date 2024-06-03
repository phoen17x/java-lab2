package com.example.library.repositories;

import com.example.library.entities.User;

import java.util.Optional;


public interface UserRepository {
    Optional<User> findByUsername(String username);
    void save(User user);
}