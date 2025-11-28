package com.nikan.weblog.repository;

import com.nikan.weblog.model.Post;
import com.nikan.weblog.model.Tag;
import com.nikan.weblog.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post,Integer> {
    Optional<Post> findBySlug(String slug);
    List<Post> findAllByAuthor(User author);
    List<Post> findAllByTagsContaining(Tag tag);
}