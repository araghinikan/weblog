package com.nikan.weblog.service;

import com.nikan.weblog.model.Post;
import com.nikan.weblog.model.Tag;
import com.nikan.weblog.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface PostService {
    Page<Post> findAll(Pageable pageable);

    List<Post> findAll(User user);

    Optional<Post> findById(int id);

    Optional<Post> findBySlug(String slug);

    Post save(Post post);

    void deleteById(int id);

    List<Post> findByTag(Tag tag);
}
