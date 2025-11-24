package com.nikan.weblog.controller;

import com.nikan.weblog.dto.RegisterDto;
import com.nikan.weblog.dto.UserDto;
import com.nikan.weblog.model.Role;
import com.nikan.weblog.model.User;
import com.nikan.weblog.service.UserServiceImpl;
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
    public List<UserDto> getAll() {
        List<User> users = userServiceImpl.findAll();
        return users.stream().map(u -> new UserDto(u.getUsername(), u.getRole()))
                .collect(Collectors.toList());
    }

    @PostMapping("/register")
    public String register(RegisterDto registerDto) {
        if(!registerDto.password().equals(registerDto.rePassword())) {
            return "redirect:/register?error=true";
        }
        User user = new User();
        user.setUsername(registerDto.username());
        user.setPassword(passwordEncoder.encode(registerDto.password()));
        user.setRole(Role.USER);

        userServiceImpl.save(user);

        return "login";
    }
}
