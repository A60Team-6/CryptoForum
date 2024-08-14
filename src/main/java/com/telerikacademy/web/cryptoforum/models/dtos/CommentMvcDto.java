package com.telerikacademy.web.cryptoforum.models.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CommentMvcDto {

    @NotNull
    @Size(max = 500, message = "Maximum comment length is 500 characters.")
    private String content;

    public CommentMvcDto() {
    }

    public String getContent() {
        return content;
    }

    public void setContent( String content) {
        this.content = content;
    }

}
