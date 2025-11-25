package com.nikan.weblog.dto;

import com.nikan.weblog.model.Status;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record PostDto(
        @NotBlank(message = "Title is required")
        @Size(max = 255, message = "Title max length is 255")
        String title,

        @NotBlank(message = "Content is required")
        String content,

        String excerpt,

        Status status,

        LocalDateTime publishedAt,

        int views,

        UserDto author,

        TagDto tag,

        CategoryDto category
) {}
