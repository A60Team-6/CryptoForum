package com.telerikacademy.web.cryptoforum.models;

import java.util.Optional;

public class FilteredUserOptions {

    private Optional<String> username;
    private Optional<String> email;
    private Optional<String> firstName;
    private Optional<String> createBefore;
    private Optional<String> createAfter;
    private Optional<String> sortBy;
    private Optional<String> sortOrder;

    public FilteredUserOptions(String username, String email, String firstName, String createBefore, String createAfter, String sortBy, String sortOrder) {
        this.username = Optional.ofNullable(username);
        this.email = Optional.ofNullable(email);
        this.firstName = Optional.ofNullable(firstName);
        this.createBefore = Optional.ofNullable(createBefore);
        this.createAfter = Optional.ofNullable(createAfter);
        this.sortBy = Optional.ofNullable(sortBy);
        this.sortOrder = Optional.ofNullable(sortOrder);
    }

    public Optional<String> getUsername() {
        return username;
    }

    public Optional<String> getEmail() {
        return email;
    }

    public Optional<String> getFirstName() {
        return firstName;
    }

    public Optional<String> getCreateBefore() {
        return createBefore;
    }

    public Optional<String> getCreateAfter() {
        return createAfter;
    }

    public Optional<String> getSortBy() {return sortBy;}

    public Optional<String> getSortOrder() {return sortOrder;}
}

