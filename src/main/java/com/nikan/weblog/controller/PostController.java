package com.nikan.weblog.controller;

import com.nikan.weblog.dto.CommentDto;
import com.nikan.weblog.dto.PostAndCommentDto;
import com.nikan.weblog.dto.PostDto;
import com.nikan.weblog.model.Category;
import com.nikan.weblog.model.Post;
import com.nikan.weblog.model.Tag;
import com.nikan.weblog.model.User;
import com.nikan.weblog.service.CategoryService;
import com.nikan.weblog.service.PostService;
import com.nikan.weblog.service.TagService;
import com.nikan.weblog.service.UserService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;
    private final UserService userService;
    private final CategoryService categoryService;
    private final TagService tagService;

    public PostController(PostService postService,
                          UserService userService,
                          CategoryService categoryService,
                          TagService tagService) {
        this.postService = postService;
        this.userService = userService;
        this.categoryService = categoryService;
        this.tagService = tagService;
    }

    private PostDto toDto(Post post) {
        Integer categoryId = post.getCategory() != null ? post.getCategory().getId() : null;
        String categoryName = post.getCategory() != null ? post.getCategory().getName() : null;
        List<Integer> tagIds = post.getTags() != null
                ? post.getTags().stream().map(Tag::getId).toList()
                : List.of();
        List<String> tagNames = post.getTags() != null
                ? post.getTags().stream().map(Tag::getName).toList()
                : List.of();

        return new PostDto(
                post.getId(),
                post.getSlug(),
                post.getTitle(),
                post.getContent(),
                post.getExcerpt(),
                post.getStatus(),
                post.getPublishedAt(),
                post.getViews(),
                categoryId,
                categoryName,
                tagIds,
                tagNames
        );
    }

    private void applyDtoToEntity(PostDto dto, Post post, Authentication auth) {
        post.setTitle(dto.title());
        post.setContent(dto.content());
        post.setExcerpt(dto.excerpt());
        post.setStatus(dto.status());
        post.setPublishedAt(dto.publishedAt());

        User user = userService.findByUsername(auth.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        post.setAuthor(user);

        if (dto.categoryId() != null) {
            Category category = categoryService.findById(dto.categoryId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));
            post.setCategory(category);
        } else {
            post.setCategory(null);
        }

        if (dto.tagIds() != null && !dto.tagIds().isEmpty()) {
            List<Tag> tags = new ArrayList<>();
            for (Integer id : dto.tagIds()) {
                Tag tag = tagService.findById(id)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tag not found: " + id));
                tags.add(tag);
            }
            post.setTags(tags);
        } else {
            post.setTags(new ArrayList<>()); // ✅ mutable list
        }
    }

    @GetMapping
    public ResponseEntity<Page<PostDto>> getAll(Pageable pageable) {
        Page<Post> posts = postService.findAll(pageable);
        return ResponseEntity.ok(posts.map(this::toDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostDto> getById(@PathVariable Integer id) {
        return postService.findById(id)
                .map(p -> ResponseEntity.ok(toDto(p)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/slug/{slug}")
    public ResponseEntity<PostDto> getBySlug(@PathVariable String slug) {
        return postService.findBySlug(slug)
                .map(p -> ResponseEntity.ok(toDto(p)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PostDto> save(@Valid @RequestBody PostDto dto, Authentication auth) {
        Post post = new Post();
        post.setViews(0);
        applyDtoToEntity(dto, post, auth);
        Post saved = postService.save(post);

        // regenerate slug after id assigned
        if (saved.getSlug() == null || saved.getSlug().isBlank()) {
            saved.setSlug(saved.getTitle() + "-" + saved.getId());
            saved = postService.save(saved);
        }

        return ResponseEntity.ok(toDto(saved));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PostDto> update(@PathVariable Integer id,
                                          @Valid @RequestBody PostDto dto,
                                          Authentication auth) {
        Optional<Post> opt = postService.findById(id);
        if (opt.isEmpty()) return ResponseEntity.notFound().build();

        Post post = opt.get();
        applyDtoToEntity(dto, post, auth);
        Post saved = postService.save(post);
        return ResponseEntity.ok(toDto(saved));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        if (postService.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        postService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/with-comments")
    public ResponseEntity<PostAndCommentDto> getPostWithComments(@PathVariable Integer id) {
        return postService.findById(id)
                .map(post -> {
                    List<CommentDto> commentDtos = post.getComments().stream()
                            .map(c -> new CommentDto(c.getId(), c.getContent(), c.getAuthorName(),
                                    c.getAuthorEmail(), c.getApproved(), c.getPost().getId() , c.getCreatedAt()))
                            .toList();
                    PostAndCommentDto dto = new PostAndCommentDto();
                    dto.setPostId(post.getId());
                    dto.setTitle(post.getTitle());
                    dto.setExcerpt(post.getExcerpt());
                    dto.setContent(post.getContent());
                    dto.setSlug(post.getSlug());
                    dto.setPublishedAt(post.getPublishedAt());
                    dto.setViews(post.getViews());
                    dto.setCategoryName(post.getCategory() != null ? post.getCategory().getName() : null);
                    dto.setAuthorUsername(post.getAuthor() != null ? post.getAuthor().getUsername() : null);
                    dto.setComments(commentDtos);
                    return ResponseEntity.ok(dto);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


}
