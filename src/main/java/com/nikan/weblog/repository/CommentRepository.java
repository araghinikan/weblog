package com.nikan.weblog.repository;

import com.nikan.weblog.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment,Integer> {
    List<Comment> findByPostId(int postId);
}