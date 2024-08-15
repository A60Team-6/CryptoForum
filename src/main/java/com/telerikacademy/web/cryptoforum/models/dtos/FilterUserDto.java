package com.telerikacademy.web.cryptoforum.models.dtos;

import java.util.Optional;

public class FilterUserDto {

    private String username;
    private String email;
    private String firstName;
    private String createBefore;
    private String createAfter;
    private String sortBy;
    private String sortOrder;

    public FilterUserDto() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getCreateBefore() {
        return createBefore;
    }

    public void setCreateBefore(String createBefore) {
        this.createBefore = createBefore;
    }

    public String getCreateAfter() {
        return createAfter;
    }

    public void setCreateAfter(String createAfter) {
        this.createAfter = createAfter;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }
}
