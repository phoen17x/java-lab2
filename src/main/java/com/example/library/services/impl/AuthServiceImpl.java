package com.example.library.services.impl;

import com.example.library.dto.auth.LoginRequest;
import com.example.library.dto.auth.LoginResponse;
import com.example.library.dto.auth.RegisterRequest;
import com.example.library.dto.auth.RegisterResponse;
import com.example.library.entities.Role;
import com.example.library.entities.User;
import com.example.library.repositories.RoleRepository;
import com.example.library.repositories.UserRepository;
import com.example.library.security.JwtManager;
import com.example.library.services.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final JwtManager jwtManager;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public LoginResponse login(LoginRequest request) throws AuthenticationException {
        log.info("Attempting to login with username: {}", request.getUsername());
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getUsername(),
                request.getPassword()));
        User user = userRepository
                .findByUsername(request.getUsername())
                .orElseThrow(() -> {
                    log.error("User not found with username: {}", request.getUsername());
                    return new RuntimeException("User not found");
                });
        String token = jwtManager.generate(user.getId(), user.getUsername(), user.getRoles());
        log.info("Login successful for user: {}", user.getUsername());
        return new LoginResponse(token);
    }

    @Override
    public RegisterResponse register(RegisterRequest request) {
        log.info("Attempting to register user: {}", request.getUsername());
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            log.error("User already exists: {}", request.getUsername());
            throw new RuntimeException("User already exists");
        }

        User user = new User();
        Role userRole = roleRepository
                .findByName("USER")
                .orElseThrow(() -> {
                    log.error("Default role 'USER' not found");
                    return new RuntimeException("Default role not found");
                });

        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.getRoles().add(userRole);

        User savedUser = userRepository.save(user);
        log.info("User registered: {}", savedUser.getUsername());
        return new RegisterResponse(savedUser.getUsername(), savedUser.getRoles());
    }
}
