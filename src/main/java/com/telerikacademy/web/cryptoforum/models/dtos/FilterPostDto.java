package com.telerikacademy.web.cryptoforum.models.dtos;

import java.util.Optional;

public class FilterPostDto {

    private String title;
    private String content;
    private Integer minLikes;
    private Integer maxLikes;
    private String createBefore;
    private String createAfter;
    private String sortBy;
    private String sortOrder;

    public FilterPostDto() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public String getCreateAfter() {
        return createAfter;
    }

    public void setCreateAfter(String createAfter) {
        this.createAfter = createAfter;
    }

    public String getCreateBefore() {
        return createBefore;
    }

    public void setCreateBefore(String createBefore) {
        this.createBefore = createBefore;
    }

    public Integer getMaxLikes() {
        return maxLikes;
    }

    public void setMaxLikes(Integer maxLikes) {
        this.maxLikes = maxLikes;
    }

    public Integer getMinLikes() {
        return minLikes;
    }

    public void setMinLikes(Integer minLikes) {
        this.minLikes = minLikes;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
