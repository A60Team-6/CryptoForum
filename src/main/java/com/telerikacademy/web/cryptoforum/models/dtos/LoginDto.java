package com.telerikacademy.web.cryptoforum.models.dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class LoginDto {

    @NotNull(message = "Username can not be empty!")
    @Size(min = 4, max = 32, message = "Username should be between 4 and 32 symbols!")
    private String username;

    @Size(min = 8, max = 32, message = "Password should be between 8 and 32 symbols!")
    @NotNull(message = "Password can not be empty!")
    private String password;

    @NotNull(message = "Username can not be empty!")
    @Size(min = 4, max = 32, message = "Username should be between 4 and 32 symbols!")
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword( String password) {
        this.password = password;
    }
}
