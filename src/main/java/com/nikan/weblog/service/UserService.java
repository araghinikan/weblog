package com.nikan.weblog.service;

import com.nikan.weblog.model.User;

import java.util.List;

public interface UserService {
    List<User> findAll();
    void save(User user);
    User findByUsername(String name);
}
