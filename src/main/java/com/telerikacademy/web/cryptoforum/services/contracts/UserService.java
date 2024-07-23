package com.telerikacademy.web.cryptoforum.services.contracts;

import com.telerikacademy.web.cryptoforum.models.User;

import java.util.List;

public interface UserService {

    List<User> getAll(User user);

    User getById(User user, int id);

    User getByUsername(String username);

    User getUserByUsername(String username, User user);

    User getByEmail(String email, User user);

    User getByFirstName(String firstName, User user);

    void createUser(User user);

    void updateUser(User user, User userForUpdate);

    void userToBeModerator(User user, User userToBeModerator);

    void deleteUser(int id);

    void blockUser(User user, int id);

    void unblockUser(User user, int id);
}
