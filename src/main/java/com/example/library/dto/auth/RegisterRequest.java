package com.example.library.dto.auth;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@AllArgsConstructor
public class RegisterRequest {

    @NotNull(message = "skidk")
    @Length(min = 3, max = 32)
    private String username;

    @NotNull()
    @Length(min = 8, max = 32)
    private String password;
}
