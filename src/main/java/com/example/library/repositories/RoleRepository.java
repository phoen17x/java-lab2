package com.example.library.repositories;

import com.example.library.entities.Role;

import java.util.Optional;

public interface RoleRepository {
    Optional<Role> findById(Long id);
    Optional<Role> findByName(String name);
}