package com.personalproject.integrated.exception;

public class CannotDeleteSelfException extends RuntimeException {
    public CannotDeleteSelfException() {
        super("You cannot delete yourself.");
    }
}
