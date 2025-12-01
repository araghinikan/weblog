package com.nikan.weblog.dto;

import com.nikan.weblog.model.Role;

public record UserDto(int id, String username, Role role) {
}