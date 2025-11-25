package com.nikan.weblog.service;

import com.nikan.weblog.model.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentService {
    List<Comment> findByPostId(int postId);
    Optional<Comment> findById(int id);
    Comment save(Comment comment);
    void deleteById(int id);
}