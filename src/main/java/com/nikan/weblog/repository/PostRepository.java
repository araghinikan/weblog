package com.nikan.weblog.repository;

import com.nikan.weblog.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post,Integer> {
    Optional<Post> findBySlug(String slug);
}
