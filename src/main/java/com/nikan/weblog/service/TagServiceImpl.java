package com.nikan.weblog.service;

import com.nikan.weblog.model.Tag;
import com.nikan.weblog.repository.TagRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TagServiceImpl implements TagService {
    private final TagRepository tagRepository;

    public TagServiceImpl(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Override
    public List<Tag> findAll() {
        return tagRepository.findAll();
    }

    @Override
    public Optional<Tag> findById(int id) {
        return tagRepository.findById(id);
    }

    @Override
    public Tag save(Tag tag) {
        return tagRepository.save(tag);
    }

    @Override
    public void deleteById(int id) {
        tagRepository.deleteById(id);
    }
}
