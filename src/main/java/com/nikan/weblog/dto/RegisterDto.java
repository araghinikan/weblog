package com.nikan.weblog.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterDto(
        @NotBlank
        @Size(min = 3, message = "username length is less that 3")
        String username,
        @NotBlank
        @Size(min = 3, message = "full name length is less that 3")
        String fullName,
        @NotBlank
        @Size(min = 3, message = "email length is less that 3")
        String email,
        @NotBlank
        @Size(min = 3, message = "password length is less that 3")
        String password,
        @Size(min = 3, message = "rePassword length is less that 3")
        @NotBlank
        String rePassword
) {}
