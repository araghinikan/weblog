package com.nikan.weblog.controller;

import com.nikan.weblog.dto.RegisterDto;
import com.nikan.weblog.dto.UserDto;
import com.nikan.weblog.model.Role;
import com.nikan.weblog.model.User;
import com.nikan.weblog.service.UserServiceImpl;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("/users")
@Controller
public class UserController {

    private final UserServiceImpl userServiceImpl;
    private final PasswordEncoder passwordEncoder;


    public UserController(UserServiceImpl userServiceImpl, PasswordEncoder passwordEncoder) {
        this.userServiceImpl = userServiceImpl;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    @ResponseBody
    public ResponseEntity<Page<UserDto>> getAll(Pageable pageable) {
        Page<User> users = userServiceImpl.findAll(pageable);
        Page<UserDto> result = users.map(u -> new UserDto(u.getUsername(), u.getRole()));
        return ResponseEntity.ok(result);
    }

    @PostMapping("/register")
    public String register(@Valid RegisterDto registerDto) {
        if(!registerDto.password().equals(registerDto.rePassword())) {
            return "redirect:/register?error=true";
        }
        User user = new User();
        user.setUsername(registerDto.username());
        user.setFullName(registerDto.fullName());
        user.setEmail(registerDto.email());
        user.setPassword(passwordEncoder.encode(registerDto.password()));
        user.setRole(Role.USER);

        userServiceImpl.save(user);

        return "login";
    }
}
