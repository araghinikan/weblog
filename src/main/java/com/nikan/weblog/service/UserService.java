package com.nikan.weblog.service;

import com.nikan.weblog.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    Page<User> findAll(Pageable pageable);
    void save(User user);
    User findByUsername(String name);
}
