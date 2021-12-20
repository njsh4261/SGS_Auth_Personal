package com.personalproject.integrated.exception;

public class EmailOrPasswordNotMatchException extends RuntimeException {
    public EmailOrPasswordNotMatchException(String message) {
        super(message);
    }
}
