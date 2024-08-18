package com.telerikacademy.web.cryptoforum.models.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.NotBlank;

public class CommentMvcDto {

    @NotNull(message = "Comment content cannot be null!")
    @Size(min = 5, max = 500, message = "Comment length must be between 5 and 500 characters!")
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
