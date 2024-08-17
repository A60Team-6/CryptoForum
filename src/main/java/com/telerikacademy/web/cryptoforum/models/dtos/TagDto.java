package com.telerikacademy.web.cryptoforum.models.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class TagDto {

    @NotNull(message = "Tag can not be null!")
    @Size(min = 2, max = 32, message = "Last name should be between 2 and 32 symbols!")
    private String name;

    public TagDto() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
