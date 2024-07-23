package com.telerikacademy.web.cryptoforum.services;

import com.telerikacademy.web.cryptoforum.models.User;
import com.telerikacademy.web.cryptoforum.repositories.contracts.UserRepository;
import com.telerikacademy.web.cryptoforum.services.contracts.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    public static final String ERROR_MESSAGE = "You are not authorized to browse user information.";


    private final UserRepository repository;

    @Autowired
    public UserServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<User> get(User user) {
        if (user.getPosition().getId() != 1) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, ERROR_MESSAGE);
        }
        return repository.get();
    }

    @Override
    public User get(int id) {
        return repository.get(id);
    }

    @Override
    public User get(String username) {
        return repository.get(username);
    }
}
