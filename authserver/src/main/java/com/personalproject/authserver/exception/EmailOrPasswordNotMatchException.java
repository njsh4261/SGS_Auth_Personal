package com.personalproject.authserver.exception;

public class EmailOrPasswordNotMatchException extends RuntimeException {
    public EmailOrPasswordNotMatchException(String message) {
        super(message);
    }
}
