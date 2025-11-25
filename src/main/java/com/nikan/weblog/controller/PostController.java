package com.nikan.weblog.controller;

import com.nikan.weblog.dto.PostDto;
import com.nikan.weblog.dto.UserDto;
import com.nikan.weblog.dto.TagDto;
import com.nikan.weblog.dto.CategoryDto;
import com.nikan.weblog.model.Post;
import com.nikan.weblog.model.User;
import com.nikan.weblog.model.Tag;
import com.nikan.weblog.model.Category;
import com.nikan.weblog.service.PostService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    private Post convertToEntity(PostDto dto) {
        Post post = new Post();
        post.setTitle(dto.title());
        post.setContent(dto.content());
        post.setExcerpt(dto.excerpt());
        post.setStatus(dto.status());
        post.setPublishedAt(dto.publishedAt());
        post.setViews(dto.views());

        UserDto userDto = dto.author();
        if (userDto != null) {
            User user = new User();
            user.setUsername(userDto.username());
            user.setRole(userDto.role());
            post.setAuthor(user);
        }

        TagDto tagDto = dto.tag();
        if (tagDto != null) {
            Tag tag = new Tag();
            tag.setName(tagDto.name());
            post.setTag(tag);
        }

        CategoryDto catDto = dto.category();
        if (catDto != null) {
            Category category = new Category();
            category.setName(catDto.name());
            category.setDescription(catDto.description());
            post.setCategory(category);
        }

        return post;
    }

    @GetMapping
    public ResponseEntity<Page<Post>> getAll(Pageable pageable) {
        Page<Post> posts = postService.findAll(pageable);
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Post> getById(@PathVariable int id) {
        Optional<Post> post = postService.findById(id);
        return post.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/slug/{slug}")
    public ResponseEntity<Post> getBySlug(@PathVariable String slug) {
        Optional<Post> post = postService.findBySlug(slug);
        return post.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Post> save(@Valid @RequestBody PostDto postDto) {
        Post post = convertToEntity(postDto);
        Post savedPost = postService.save(post);
        return ResponseEntity.ok(savedPost);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Post> updatePost(@PathVariable int id, @Valid @RequestBody PostDto postDto) {
        Optional<Post> optionalPost = postService.findById(id);
        if (optionalPost.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Post existingPost = optionalPost.get();

        existingPost.setTitle(postDto.title());
        existingPost.setContent(postDto.content());
        existingPost.setExcerpt(postDto.excerpt());
        existingPost.setStatus(postDto.status());
        existingPost.setPublishedAt(postDto.publishedAt());
        existingPost.setViews(postDto.views());

        UserDto userDto = postDto.author();
        if (userDto != null) {
            User author = new User();
            author.setUsername(userDto.username());
            author.setRole(userDto.role());
            existingPost.setAuthor(author);
        } else {
            existingPost.setAuthor(null);
        }

        TagDto tagDto = postDto.tag();
        if (tagDto != null) {
            Tag tag = new Tag();
            tag.setName(tagDto.name());
            existingPost.setTag(tag);
        } else {
            existingPost.setTag(null);
        }

        CategoryDto catDto = postDto.category();
        if (catDto != null) {
            Category category = new Category();
            category.setName(catDto.name());
            category.setDescription(catDto.description());
            existingPost.setCategory(category);
        } else {
            existingPost.setCategory(null);
        }

        Post savedPost = postService.save(existingPost);
        return ResponseEntity.ok(savedPost);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable int id) {
        postService.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
