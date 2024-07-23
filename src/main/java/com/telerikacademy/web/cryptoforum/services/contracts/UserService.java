package com.telerikacademy.web.cryptoforum.services.contracts;

import com.telerikacademy.web.cryptoforum.models.User;

import java.util.List;

public interface UserService {

    List<User> get(User user);

    User get(int id);

    User get(String username);
}
