package com.telerikacademy.web.cryptoforum.models.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CommentDto {

    @NotNull
    @Size(max = 500, message = "Maximum comment length is 500 characters.")
    private String content;

    private Integer parentId;

    private Integer postId;

    public CommentDto() {
    }

    public String getContent() {
        return content;
    }

    public Integer getParentId() {
        return parentId;
    }

    public Integer getPostId() {
        return postId;
    }
}
