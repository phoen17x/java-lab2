package com.example.library.controllers;

import com.example.library.dto.auth.LoginRequest;
import com.example.library.dto.auth.LoginResponse;
import com.example.library.dto.auth.RegisterRequest;
import com.example.library.dto.auth.RegisterResponse;
import com.example.library.services.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public LoginResponse login(@Validated @RequestBody LoginRequest request) {
        log.info("Received login request: {}", request);
        LoginResponse response = authService.login(request);
        log.info("Login response: {}", response);
        return response;
    }

    @PostMapping(value = "/register", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public RegisterResponse register(@Validated @RequestBody RegisterRequest request) {
        log.info("Received register request: {}", request);
        RegisterResponse response = authService.register(request);
        log.info("Register response: {}", response);
        return response;
    }
}