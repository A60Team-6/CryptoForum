package com.telerikacademy.web.cryptoforum.models.dtos;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;


public class PostOutDto{

    private String user;

    private String title;

    private String content;

    private int likes;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private List<String> comments;

    public PostOutDto() {
    }

    public String getUser() {
        return user;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public int getLikes() {
        return likes;
    }

    public List<String> getComments() {
        return comments;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setComments(List<String> comments) {
        this.comments = comments;
    }
}
