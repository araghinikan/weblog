package com.nikan.weblog.controller;

import com.nikan.weblog.dto.CommentDto;
import com.nikan.weblog.model.Comment;
import com.nikan.weblog.model.Post;
import com.nikan.weblog.service.CommentService;
import com.nikan.weblog.service.PostService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;
    private final PostService postService;

    public CommentController(CommentService commentService,  PostService postService) {
        this.commentService = commentService;
        this.postService = postService;
    }

        @GetMapping("/post/{postId}")
        public ResponseEntity<List<CommentDto>> getByPostId(@PathVariable Integer postId) {
            List<Comment> comments = commentService.findByPostId(postId);
            List<CommentDto> dtos = comments.stream()
                    .map(c -> new CommentDto(c.getId(), c.getContent(), c.getAuthorName(),
                            c.getAuthorEmail(), c.getApproved(), c.getPost().getId(), c.getCreatedAt()))
                    .toList();
            return ResponseEntity.ok(dtos);
        }


    @GetMapping("/{id}")
    public ResponseEntity<Comment> getCommentById(@PathVariable int id) {
        Optional<Comment> comment = commentService.findById(id);
        return comment.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> saveComment(@Valid @RequestBody CommentDto commentDto) {
        try {
            Comment comment = convertToEntity(commentDto);
            comment.setApproved(0);
            Comment savedComment = commentService.save(comment);
            return ResponseEntity.ok(savedComment);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("خطا در ذخیره کامنت: " + e.getMessage());
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteComment(@PathVariable int id) {
        commentService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/list")
    public ResponseEntity<Page<CommentDto>> getAllComments(Pageable pageable) {
        Page<Comment> comments = commentService.findAll(pageable);
        Page<CommentDto> dtos = comments.map(c -> new CommentDto(
                c.getId(),
                c.getContent(),
                c.getAuthorName(),
                c.getAuthorEmail(),
                c.getApproved(),
                c.getPost() != null ? c.getPost().getId() : 0,
                c.getCreatedAt()
        ));
        return ResponseEntity.ok(dtos);
    }

    @PutMapping("/{id}/approve")
    public ResponseEntity<?> approveComment(@PathVariable int id) {
        Optional<Comment> optionalComment = commentService.findById(id);
        if (optionalComment.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Comment comment = optionalComment.get();
        comment.setApproved(1);
        Comment updated = commentService.save(comment);

        return ResponseEntity.ok(new CommentDto(
                updated.getId(),
                updated.getContent(),
                updated.getAuthorName(),
                updated.getAuthorEmail(),
                updated.getApproved(),
                updated.getPost() != null ? updated.getPost().getId() : 0,
                updated.getCreatedAt()
        ));
    }

    private Comment convertToEntity(CommentDto dto) {
        Comment comment = new Comment();
        comment.setContent(dto.content());
        comment.setAuthorName(dto.authorName());
        comment.setAuthorEmail(dto.authorEmail());

        if (dto.postId() > 0) {
            Post post = postService.findById(dto.postId())
                    .orElseThrow(() -> new IllegalArgumentException("Post not found with id " + dto.postId()));
            comment.setPost(post);
        }
        return comment;
    }
}
