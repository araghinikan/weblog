package com.nikan.weblog.service;

import com.nikan.weblog.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface UserService {
    Page<User> findAll(Pageable pageable);
    void save(User user);
    Optional<User> findByUsername(String name);
}
