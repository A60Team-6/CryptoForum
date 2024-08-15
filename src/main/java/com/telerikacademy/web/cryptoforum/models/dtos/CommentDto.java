package com.telerikacademy.web.cryptoforum.models.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CommentDto {

    @NotNull
    @Size(max = 500, message = "Maximum comment length is 500 characters.")
    private String content;

    @NotNull
    private Integer id;

    public CommentDto() {
    }

    public String getContent() {
        return content;
    }

    public Integer getId() {
        return id;
    }

    public void setContent( String content) {
        this.content = content;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
