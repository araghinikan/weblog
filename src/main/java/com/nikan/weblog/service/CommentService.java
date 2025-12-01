package com.nikan.weblog.service;

import com.nikan.weblog.model.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface CommentService {
    List<Comment> findByPostId(int postId);
    Optional<Comment> findById(int id);
    Comment save(Comment comment);
    void deleteById(int id);
    Page<Comment> findAll(Pageable pageable);
    Comment approveComment(int id);
}