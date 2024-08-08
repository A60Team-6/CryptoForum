package com.telerikacademy.web.cryptoforum.exceptions;

public class AuthenticationFailureException extends RuntimeException{

    public AuthenticationFailureException(String message){
        super(message);
    }
}
