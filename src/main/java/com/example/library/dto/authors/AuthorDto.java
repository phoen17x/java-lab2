package com.example.library.dto.authors;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AuthorDto {

    @NotNull
    private String name;

    @NotNull
    private String bio;
}
