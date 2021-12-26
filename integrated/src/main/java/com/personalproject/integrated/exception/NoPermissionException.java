package com.personalproject.integrated.exception;

public class NoPermissionException extends RuntimeException{
    public NoPermissionException() {
        super("No permission is granted.");
    }
}
