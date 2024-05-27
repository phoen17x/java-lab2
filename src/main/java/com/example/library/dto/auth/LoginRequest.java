package com.example.library.dto.auth;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@AllArgsConstructor
public class LoginRequest {

    @NotNull
    private String username;

    @NotNull
    @Length(min = 8, max = 32)
    private String password;
}
