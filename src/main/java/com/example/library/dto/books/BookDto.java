package com.example.library.dto.books;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class BookDto {

    @NotNull
    private String title;

    @NotNull
    private Long authorId;
}
