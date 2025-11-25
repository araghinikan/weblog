package com.nikan.weblog.service;

import com.nikan.weblog.model.Tag;

import java.util.List;
import java.util.Optional;

public interface TagService {
    List<Tag> findAll();
    Optional<Tag> findById(int id);
    Tag save(Tag tag);
    void deleteById(int id);
}