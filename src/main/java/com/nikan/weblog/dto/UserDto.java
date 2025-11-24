package com.nikan.weblog.dto;

import com.nikan.weblog.model.Role;

public record UserDto(String username, Role role) {
}