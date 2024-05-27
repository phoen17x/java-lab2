package com.example.library.dto.auth;

import com.example.library.entities.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
public class RegisterResponse {
    private String username;
    private Set<Role> roles;
}
