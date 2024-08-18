package com.telerikacademy.web.cryptoforum.repositories.contracts;

import com.telerikacademy.web.cryptoforum.models.AdminPhone;
import com.telerikacademy.web.cryptoforum.models.FilteredUserOptions;
import com.telerikacademy.web.cryptoforum.models.User;

import java.util.List;

public interface UserRepository {

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

    int countFilteredUsers(FilteredUserOptions filteredUserOptions);

    List<User> getAll();

    List<User> getAll(FilteredUserOptions filteredUserOptions, int page, int pageSize);

    List<User> getAll(FilteredUserOptions filteredUserOptions);

    User getById(int id);

    User getByUsername(String username);

    User getByEmail(String email);

    void update(User user);

    User getByFirstName(String firstName);

    void createUser(User user);

    void updateTo(User user);

    void delete(int id);

    void block(User user);

    void unblock(User user);

//    void addPhoneNumber(AdminPhone adminPhone);
}
