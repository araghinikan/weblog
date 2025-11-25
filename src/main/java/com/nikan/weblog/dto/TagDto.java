package com.nikan.weblog.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record TagDto(
        @NotBlank(message = "Name is required")
        @Size(max = 255, message = "Name max length is 255")
        String name
) {}
