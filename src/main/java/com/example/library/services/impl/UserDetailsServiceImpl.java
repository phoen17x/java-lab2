package com.example.library.services.impl;

import com.example.library.entities.User;
import com.example.library.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        log.info("Loading user details for username: {}", username);

        User user = userRepository.findByUsername(username).orElseThrow();

        log.info("User found: {}", user);
        return new UserDetailsImpl(user, user.getRoles());
    }
}