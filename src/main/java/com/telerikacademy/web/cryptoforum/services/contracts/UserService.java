package com.telerikacademy.web.cryptoforum.services.contracts;

import com.telerikacademy.web.cryptoforum.models.AdminPhone;
import com.telerikacademy.web.cryptoforum.models.FilteredUserOptions;
import com.telerikacademy.web.cryptoforum.models.User;

import java.util.List;

public interface UserService {

    int countUsers();

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

    List<User> getAll(FilteredUserOptions filteredUserOptions, User user);

    List<User> getAll(FilteredUserOptions filteredUserOptions, int page, int pageSize);

    int countFilteredUsers(FilteredUserOptions options);

    User getById(User user, int id);

    User getByUsername(String username);

    User getUserByUsername(String username, User user);

    User getByEmail(String email, User user);

    User getByFirstName(String firstName, User user);

    void createUser(User user);

    void updateUser(User user, User userForUpdate);

    void userToBeModerator(User user, User userToBeModerator);

    void userToBeNotModerator(User user, User userToBeNotModerator);

    void deleteUser(User user, int id);

    void blockUser(User user, int id);

    void unblockUser(User user, int id);

}
