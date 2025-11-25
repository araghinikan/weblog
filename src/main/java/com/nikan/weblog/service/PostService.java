package com.nikan.weblog.service;

import com.nikan.weblog.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface PostService {
    Page<Post> findAll(Pageable pageable);

    Optional<Post> findById(int id);

    Optional<Post> findBySlug(String slug);

    Post save(Post post);

    void deleteById(int id);
}
