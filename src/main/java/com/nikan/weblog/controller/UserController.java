package com.nikan.weblog.controller;

import com.nikan.weblog.dto.PostDto;
import com.nikan.weblog.dto.RegisterDto;
import com.nikan.weblog.dto.UserAndPostDto;
import com.nikan.weblog.dto.UserDto;
import com.nikan.weblog.model.Post;
import com.nikan.weblog.model.Role;
import com.nikan.weblog.model.User;
import com.nikan.weblog.service.PostServiceImpl;
import com.nikan.weblog.service.UserServiceImpl;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequestMapping("/users")
@Controller
public class UserController {

    private final UserServiceImpl userServiceImpl;
    private final PasswordEncoder passwordEncoder;
    private final PostServiceImpl postServiceImpl;


    public UserController(UserServiceImpl userServiceImpl, PasswordEncoder passwordEncoder, PostServiceImpl postServiceImpl) {
        this.userServiceImpl = userServiceImpl;
        this.passwordEncoder = passwordEncoder;
        this.postServiceImpl = postServiceImpl;
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

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseBody
    public ResponseEntity<Page<UserDto>> getAllUsers(Pageable pageable) {
        Page<User> users = userServiceImpl.findAll(pageable);
        Page<UserDto> result = users.map(u -> new UserDto(u.getId(), u.getUsername(), u.getRole()));
        return ResponseEntity.ok(result);
    }

    @PutMapping("/{id}/role")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<User> updateUserRole(@PathVariable int id, @RequestParam Role role) {
        User user = userServiceImpl.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id " + id));

        user.setRole(role);
        User updated = userServiceImpl.save(user);

        return ResponseEntity.ok(updated);
    }

    @GetMapping("/posts/{username}")
    @ResponseBody
    public ResponseEntity<List<UserAndPostDto>> posts(@PathVariable String username) {
        User user = userServiceImpl.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        List<Post> posts = postServiceImpl.findAll(user);
        List<UserAndPostDto> userAndPostDtos = posts.stream().map(post -> {
            UserAndPostDto userAndPostDto = new UserAndPostDto();
            userAndPostDto.setId(user.getId());
            userAndPostDto.setPostId(post.getId());
            userAndPostDto.setTitle(post.getTitle());
            userAndPostDto.setExcerpt(post.getExcerpt());
            userAndPostDto.setStatus(post.getStatus());
            userAndPostDto.setPublishedAt(post.getPublishedAt());
            userAndPostDto.setViews(post.getViews());
            userAndPostDto.setUsername(post.getAuthor().getUsername());
            userAndPostDto.setRole(post.getAuthor().getRole());
            return userAndPostDto;
        }).collect(Collectors.toList());
        return ResponseEntity.ok(userAndPostDtos);
    }
}
