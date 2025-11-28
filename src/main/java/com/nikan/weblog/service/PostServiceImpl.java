package com.nikan.weblog.service;

import com.nikan.weblog.model.Post;
import com.nikan.weblog.model.Tag;
import com.nikan.weblog.model.User;
import com.nikan.weblog.repository.PostRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;

    public PostServiceImpl(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Override
    public Page<Post> findAll(Pageable pageable) {
        return postRepository.findAll(pageable);
    }

    @Override
    public List<Post> findAll(User user) {
        return postRepository.findAllByAuthor(user);
    }

    @Override
    public Optional<Post> findById(int id) {
        return postRepository.findById(id);
    }

    @Override
    public Optional<Post> findBySlug(String slug) {
        return postRepository.findBySlug(slug);
    }

    @Override
    public Post save(Post post) {
        return postRepository.save(post);
    }

    @Override
    public void deleteById(int id) {
        postRepository.deleteById(id);
    }

    @Override
    public List<Post> findByTag(Tag tag){
        return postRepository.findAllByTagsContaining(tag);
    }
}
