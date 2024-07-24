package com.telerikacademy.web.cryptoforum.models;

import java.util.Optional;

public class FilteredUserOptions {

    private Optional<String> username;
    private Optional<String> email;
    private Optional<String> firstname;

    public FilteredUserOptions(String username, String email, String firstname) {
        this.username = Optional.ofNullable(username);
        this.email = Optional.ofNullable(email);
        this.firstname = Optional.ofNullable(firstname);
    }

    public Optional<String> getUsername() {
        return username;
    }

    public Optional<String> getEmail() {
        return email;
    }

    public Optional<String> getFirstname() {
        return firstname;
    }
}
