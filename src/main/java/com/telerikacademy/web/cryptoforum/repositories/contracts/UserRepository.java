package com.telerikacademy.web.cryptoforum.repositories.contracts;

import com.telerikacademy.web.cryptoforum.models.User;

import java.util.List;

public interface UserRepository {
    List<User> getAll();

    User getById(int id);

    User getByUsername(String username);

    User getByEmail(String email);

    void update(User user);

    User getByFirstName(String firstName);

    void createUser(User user);

    void updateTo(User user);
}
