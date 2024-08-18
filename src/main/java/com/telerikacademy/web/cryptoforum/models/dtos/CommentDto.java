package com.telerikacademy.web.cryptoforum.models.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.NotBlank;

public class CommentDto {


    @NotNull(message = "Comment content cannot be null!")
    @NotBlank(message = "Comment content cannot be blank!")
    @Size(min = 5, max = 500, message = "Comment length must be between 5 and 500 characters!")
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
