package com.telerikacademy.web.cryptoforum.exceptions;

public class AuthenticationException extends RuntimeException{

    public AuthenticationException (String message){
        super(message);
    }
}
