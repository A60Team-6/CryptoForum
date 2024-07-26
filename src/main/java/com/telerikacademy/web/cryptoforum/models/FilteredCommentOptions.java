package com.telerikacademy.web.cryptoforum.models;

import java.util.Optional;

public class FilteredCommentOptions {

    private Optional<String> content;
    private Optional<String> createBefore;
    private Optional<String> createAfter;
    private Optional<String> sortBy;
    private Optional<String> sortOrder;

    public FilteredCommentOptions(String content,
                                  String createBefore,
                                  String createAfter,
                                  String sortBy,
                                  String sortOrder) {
        this.content = Optional.ofNullable(content);
        this.createAfter = Optional.ofNullable(createAfter);
        this.createBefore = Optional.ofNullable(createBefore);
        this.sortBy = Optional.ofNullable(sortBy);
        this.sortOrder = Optional.ofNullable(sortOrder);
    }

    public Optional<String> getContent() {
        return content;
    }

    public Optional<String> getCreateBefore() {
        return createBefore;
    }

    public Optional<String> getCreateAfter() {
        return createAfter;
    }

    public Optional<String> getSortBy() {
        return sortBy;
    }

    public Optional<String> getSortOrder() {
        return sortOrder;
    }

}
