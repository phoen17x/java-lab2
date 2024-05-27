package com.example.library.security;

import com.example.library.entities.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Set;

@Component
@Slf4j
public class JwtManager {

    private final UserDetailsService userDetailsService;
    private final SecretKey secretKey;
    private final Long lifetime;

    public JwtManager(UserDetailsService userDetailsService, JwtSettings jwtSettings) {
        this.userDetailsService = userDetailsService;
        this.secretKey = Keys.hmacShaKeyFor(jwtSettings.getSecret().getBytes());
        this.lifetime = jwtSettings.getLifetime();
    }

    public String generate(Long userId, String username, Set<Role> roles) {
        Claims claims = Jwts
                .claims()
                .subject(username)
                .add("id", userId)
                .add("roles", roles.stream().map(Role::getName).toList())
                .build();

        Date expirationDate = Date.from(Instant.now().plus(lifetime, ChronoUnit.HOURS));

        return Jwts
                .builder()
                .claims(claims)
                .expiration(expirationDate)
                .signWith(secretKey)
                .compact();
    }

    public boolean validate(String token) {
        try {
            Jws<Claims> claims = Jwts
                    .parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token);

            return claims
                    .getPayload()
                    .getExpiration()
                    .after(new Date());
        } catch (Exception exception) {
            log.info("Couldn't validate token: {}", exception.getMessage());
            return false;
        }
    }

    public Authentication getAuthentication(String token) {
        String username = extractUsername(token);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(
                userDetails,
                "",
                userDetails.getAuthorities()
        );
    }

    private String extractUsername(String token) {
        return Jwts
                .parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }
}
