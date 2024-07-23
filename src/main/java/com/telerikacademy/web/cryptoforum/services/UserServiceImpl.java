package com.telerikacademy.web.cryptoforum.services;

import com.telerikacademy.web.cryptoforum.exceptions.DuplicateEntityException;
import com.telerikacademy.web.cryptoforum.exceptions.EntityNotFoundException;
import com.telerikacademy.web.cryptoforum.helpers.PermissionHelper;
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
    public List<User> getAll(User user) {
        if (user.getPosition().getId() != 1) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, ERROR_MESSAGE);
        }
        return repository.getAll();
    }

    @Override
    public User getById(User user, int id) {
        PermissionHelper.isAdmin(user, "This user is not an admin!");
        return repository.getById(id);
    }

    @Override
    public User getByUsername(String username) {
        return repository.getByUsername(username);
    }

    @Override
    public User getUserByUsername(String username, User user) {
        PermissionHelper.isAdmin(user, "User is not an admin!");
        return repository.getByUsername(username);
    }

    public User getByEmail(String email, User user){
        PermissionHelper.isAdmin(user, "User is not an admin!");
        return repository.getByEmail(email);
    }

    public User getByFirstName(String firstName, User user){
        PermissionHelper.isAdmin(user, "User is not an admin!");
        return repository.getByFirstName(firstName);
    }

    @Override
    public void createUser(User user){
        boolean duplicateExists = true;
        try {
            repository.getById(user.getId());
        } catch (EntityNotFoundException e) {
            duplicateExists = false;
        }

        if (duplicateExists) {
            throw new DuplicateEntityException("Beer", "name", user.getUsername());
        }

        repository.createUser(user);
    }

    @Override
    public void updateUser(User user, User userForUpdate){
        PermissionHelper.isAdminOrSameUser(user, userForUpdate, "This user is not admin nor owner!");

        boolean duplicateExists = true;
        try {
            User existingUser = repository.getByUsername(userForUpdate.getUsername());
            if (existingUser.getId() == userForUpdate.getId()) {
                duplicateExists = false;
            }
        } catch (EntityNotFoundException e) {
            duplicateExists = false;
        }

        if (duplicateExists) {
            throw new DuplicateEntityException("User", "username", userForUpdate.getUsername());
        }

        repository.update(userForUpdate);
    }

    public void userToBeModerator(User user, User userToBeModerator){
        PermissionHelper.isAdmin(user, "This user is not an admin!");

        boolean isAlreadyUserOrAdmin = true;
        try {
            User userToModerator = repository.getByUsername(userToBeModerator.getUsername());
            if(!userToModerator.getPosition().getName().equals("admin") && !userToModerator.getPosition().getName().equals("moderator")){
                isAlreadyUserOrAdmin = false;
            }
        }catch (EntityNotFoundException e){
            isAlreadyUserOrAdmin = false;
        }

        if(isAlreadyUserOrAdmin){
            throw new DuplicateEntityException("User", "username", userToBeModerator.getUsername());
        }else {
            userToBeModerator.getPosition().setName("moderator");
            userToBeModerator.getPosition().setId(2);
        }

        repository.updateTo(userToBeModerator);

    }


}
