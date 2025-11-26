package com.nikan.weblog.controller;

import com.nikan.weblog.service.PostService;
import com.nikan.weblog.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    private final PostService postService;

    public PageController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/")
    public String index(Model model, Pageable pageable) {
        Page<Post> posts = postService.findAll(pageable);
        model.addAttribute("posts", posts);
        return "index";
    }

    @GetMapping("/auth/login")
    public String login() {
        return "login";
    }

    @GetMapping("/auth/register")
    public String register() {
        return "register";
    }

    @GetMapping("/admin/posts")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String adminPosts() {
        return "admin/posts-list";
    }

    @GetMapping("/admin/posts/edit")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String adminPostEdit() {
        return "admin/post-form";
    }

    @GetMapping("/admin/categories-tags")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String adminCategoriesTags() {
        return "admin/categories-tags";
    }

    @GetMapping("/admin/comments")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String adminComments() {
        return "admin/comments";
    }
}
