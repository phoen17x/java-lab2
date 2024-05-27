package com.example.library.services;

import com.example.library.dto.auth.LoginRequest;
import com.example.library.dto.auth.LoginResponse;
import com.example.library.dto.auth.RegisterRequest;
import com.example.library.dto.auth.RegisterResponse;
import org.springframework.security.core.AuthenticationException;

public interface AuthService {

    LoginResponse login(LoginRequest request) throws AuthenticationException;

    RegisterResponse register(RegisterRequest request);
}
