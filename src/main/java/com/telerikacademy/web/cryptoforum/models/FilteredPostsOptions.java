package com.telerikacademy.web.cryptoforum.models;

import java.util.Optional;

public class FilteredPostsOptions {

    private Optional<String> title;
    private Optional<String> content;
    private Optional<Integer> minLikes;
    private Optional<Integer> maxLikes;
    private Optional<String> createBefore;
    private Optional<String> createAfter;
    private Optional<String> sortBy;
    private Optional<String> sortOrder;

    public FilteredPostsOptions(String title,
                                String content,
                                Integer minLikes,
                                Integer maxLikes,
                                String createBefore,
                                String createAfter,
                                String sortBy,
                                String sortOrder) {
        this.title = Optional.ofNullable(title);
        this.content = Optional.ofNullable(content);
        this.minLikes = Optional.ofNullable(minLikes);
        this.maxLikes = Optional.ofNullable(maxLikes);
        this.createAfter = Optional.ofNullable(createAfter);
        this.createBefore = Optional.ofNullable(createBefore);
        this.sortBy = Optional.ofNullable(sortBy);
        this.sortOrder = Optional.ofNullable(sortOrder);
    }

    public FilteredPostsOptions() {
        this(null, null, null, null, null, null, null, null);
    }

    public Optional<String> getTitle() {
        return title;
    }

    public Optional<String> getContent() {
        return content;
    }

    public Optional<Integer> getMinLikes() {
        return minLikes;
    }

    public Optional<Integer> getMaxLikes() {
        return maxLikes;
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
