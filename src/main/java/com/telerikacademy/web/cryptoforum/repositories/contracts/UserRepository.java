package com.telerikacademy.web.cryptoforum.repositories.contracts;

import com.telerikacademy.web.cryptoforum.models.User;

import java.util.List;

public interface UserRepository {
    List<User> get();

    User get(int id);

    User get(String username);

    void update(User user);
}
