package com.nikan.weblog.controller;

import com.nikan.weblog.dto.CommentDto;
import com.nikan.weblog.model.Comment;
import com.nikan.weblog.model.Post;
import com.nikan.weblog.service.CommentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<List<Comment>> getCommentsByPost(@PathVariable int postId) {
        List<Comment> comments = commentService.findByPostId(postId);
        return ResponseEntity.ok(comments);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Comment> getCommentById(@PathVariable int id) {
        Optional<Comment> comment = commentService.findById(id);
        return comment.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Comment> saveComment(@Valid @RequestBody CommentDto commentDto) {
        Comment comment = convertToEntity(commentDto);
        Comment savedComment = commentService.save(comment);
        return ResponseEntity.ok(savedComment);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteComment(@PathVariable int id) {
        commentService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    private Comment convertToEntity(CommentDto dto) {
        Comment comment = new Comment();
        comment.setContent(dto.content());
        comment.setAuthorName(dto.authorName());
        comment.setAuthorEmail(dto.authorEmail());
        comment.setApproved(dto.approved());

        Post post = dto.post();
        if (post != null) {
            comment.setPost(post);
        }
        return comment;
    }
}
