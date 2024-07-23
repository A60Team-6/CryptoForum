package com.telerikacademy.web.cryptoforum.exceptions;

public class UnauthorizedOperationException extends RuntimeException{

    public UnauthorizedOperationException(String message) {
        super(message);
    }
}
