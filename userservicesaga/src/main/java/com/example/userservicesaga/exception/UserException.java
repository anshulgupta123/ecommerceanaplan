package com.example.userservicesaga.exception;

public class UserException extends RuntimeException {

    String message;

    public UserException(String message) {
        super(message);
        this.message = message;

    }

    @Override
    public String getMessage() {
        return message;
    }
}

