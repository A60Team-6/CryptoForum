package com.telerikacademy.web.cryptoforum.services;

import com.telerikacademy.web.cryptoforum.exceptions.BlockedException;
import com.telerikacademy.web.cryptoforum.exceptions.DuplicateEntityException;
import com.telerikacademy.web.cryptoforum.exceptions.EntityNotFoundException;
import com.telerikacademy.web.cryptoforum.exceptions.UnauthorizedOperationException;
import com.telerikacademy.web.cryptoforum.helpers.PermissionHelper;
import com.telerikacademy.web.cryptoforum.models.FilteredUserOptions;
import com.telerikacademy.web.cryptoforum.models.User;
import com.telerikacademy.web.cryptoforum.repositories.contracts.UserRepository;
import com.telerikacademy.web.cryptoforum.services.contracts.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public List<User> getAll(FilteredUserOptions filteredUserOptions, User user) {
        PermissionHelper.isAdminOrModerator(user, ERROR_MESSAGE);
        return repository.getAll(filteredUserOptions);
    }

    @Override
    public List<User> getAll(FilteredUserOptions filteredUserOptions, int page, int pageSize){
        return repository.getAll(filteredUserOptions, page, pageSize);
    }

    @Override
    public int countFilteredUsers(FilteredUserOptions options) {
        return repository.countFilteredUsers(options);
    }

    @Override
    public User getById(User user, int id) {
        PermissionHelper.isAdminOrSameUserOrModerator(user, id, ERROR_MESSAGE);
        return repository.getById(id);
    }

    @Override
    public User getByUsername(String username) {
        return repository.getByUsername(username);
    }

    @Override
    public User getUserByUsername(String username, User user) {
        PermissionHelper.isAdmin(user, ERROR_MESSAGE);
        return repository.getByUsername(username);
    }

    public User getByEmail(String email, User user){
        PermissionHelper.isAdmin(user, ERROR_MESSAGE);
        return repository.getByEmail(email);
    }

    public User getByFirstName(String firstName, User user){
        PermissionHelper.isAdmin(user, ERROR_MESSAGE);
        return repository.getByFirstName(firstName);
    }

    @Override
    public void createUser(User user){
        boolean duplicateExists = true;
        try {
            repository.getByUsername(user.getUsername());
            repository.getByEmail(user.getEmail());
        } catch (EntityNotFoundException e) {
            duplicateExists = false;
        }

        if (duplicateExists) {
            throw new DuplicateEntityException("User", "name", user.getUsername());
        }

        repository.createUser(user);
    }

    @Override
    public void updateUser(User user, User userForUpdate){
        PermissionHelper.isBlocked(user, "User is not blocked!");
        PermissionHelper.isSameUser(user, userForUpdate, "This user is not an owner!");

        boolean duplicateExists = true;
        try {
            User existingUser = repository.getByEmail(userForUpdate.getEmail());
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

    @Override
    public void userToBeModerator(User user, User userToBeModerator){
        PermissionHelper.isAdmin(user, "This user is not an admin!");

        boolean isAlreadyUserOrAdmin = true;
        try {
            User userToModerator = repository.getByUsername(userToBeModerator.getUsername());
            if(!userToModerator.getPosition().getName().equals("admin") && !userToModerator.getPosition().getName().equals("moderator")){
                isAlreadyUserOrAdmin = false;
            }
        }catch (EntityNotFoundException e){
            throw new EntityNotFoundException("User", userToBeModerator.getId());
        }

        if(isAlreadyUserOrAdmin){
            throw new UnauthorizedOperationException("User is moderator or admin!");
        }else {
            userToBeModerator.getPosition().setName("moderator");
            userToBeModerator.getPosition().setId(2);
        }

        repository.updateTo(userToBeModerator);
    }

    public void userToBeNotModerator(User user, User userToBeNotModerator){
        PermissionHelper.isAdmin(user, "This user is not an admin!");

        boolean isModerator = true;
        try {
            User userFromModerator = repository.getByUsername(userToBeNotModerator.getUsername());
            if(userFromModerator.getPosition().getName().equals("user") || userFromModerator.getPosition().getName().equals("admin")){
                isModerator = false;
            }
        }catch (EntityNotFoundException e){
            throw new EntityNotFoundException("User", userToBeNotModerator.getId());
        }

        if(!isModerator){
            throw new UnsupportedOperationException("User is not moderator!");
        }else {
            userToBeNotModerator.getPosition().setName("user");
            userToBeNotModerator.getPosition().setId(3);
        }

        repository.updateTo(userToBeNotModerator);
    }

    public void blockUser(User user, int id){
        PermissionHelper.isAdminOrModerator(user, "This user is neither admin nor moderator");

        boolean isAlreadyBlocked = false;
        try {
            User userToBlock = repository.getById(id);
            if(userToBlock.isBlocked()){
                isAlreadyBlocked = true;
            }
            userToBlock.setBlocked(true);
            repository.block(userToBlock);
        }catch (EntityNotFoundException e){
            throw new EntityNotFoundException("User", id);
        }

        if(isAlreadyBlocked){
            throw new BlockedException("This user is already blocked!");

        }
    }

    public void unblockUser(User user, int id){
        PermissionHelper.isAdminOrModerator(user, "This user is neither admin nor moderator");

        boolean isAlreadyBlocked = true;
        try {
            User userToBlock = repository.getById(id);
            if(!userToBlock.isBlocked()){
                isAlreadyBlocked = false;
            }
            userToBlock.setBlocked(false);
            repository.unblock(userToBlock);
        }catch (EntityNotFoundException e){
            throw new EntityNotFoundException("User", id);
        }

        if(!isAlreadyBlocked){
            throw new BlockedException("This User is not blocked!");
        }
    }

    @Override
    public void deleteUser(User user, int id){
        User userToDelete = repository.getById(id);
        PermissionHelper.isAdminOrSameUser(user, userToDelete, "You are not admin or same user!");

        repository.delete(id);
    }
}
