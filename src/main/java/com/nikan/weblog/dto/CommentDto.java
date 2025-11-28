package com.nikan.weblog.dto;

import com.nikan.weblog.model.Post;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CommentDto(
        @NotBlank(message = "Content is required")
        String content,

        @NotBlank(message = "Author name is required")
        @Size(max = 255, message = "Author name max length is 255")
        String authorName,

        @Email(message = "Invalid email format")
        @NotBlank(message = "Author email is required")
        String authorEmail,

        @NotBlank(message = "Approved field is required")
        int approved,

        Post post
) {}
