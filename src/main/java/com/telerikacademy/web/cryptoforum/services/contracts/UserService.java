package com.telerikacademy.web.cryptoforum.services.contracts;

import com.telerikacademy.web.cryptoforum.models.User;

import java.util.List;

public interface UserService {

    List<User> getAll(User user);

    User getById(int id);

    User getByUsername(String username);
}
