package com.example.library.security;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Setter
@Getter
@ConfigurationProperties(prefix = "security.jwt")
@Component
public class JwtSettings {

    private String secret;
    private Long lifetime;
}
