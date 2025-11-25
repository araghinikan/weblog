package com.nikan.weblog.service;

import com.nikan.weblog.model.Comment;
import com.nikan.weblog.repository.CommentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;

    public CommentServiceImpl(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Override
    public List<Comment> findByPostId(int postId) {
        return commentRepository.findByPostId(postId);
    }

    @Override
    public Optional<Comment> findById(int id) {
        return commentRepository.findById(id);
    }

    @Override
    public Comment save(Comment comment) {
        return commentRepository.save(comment);
    }

    @Override
    public void deleteById(int id) {
        commentRepository.deleteById(id);
    }
}
