package com.nikan.weblog.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CommentDto(
        int id,
        @NotBlank(message = "Content is required")
        String content,

        @NotBlank(message = "Author name is required")
        @Size(max = 255, message = "Author name max length is 255")
        String authorName,

        @Email(message = "Invalid email format")
        @NotBlank(message = "Author email is required")
        String authorEmail,

        int approved,

        int postId,

         String createdAt
) {}
