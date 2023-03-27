package com.example.orderservice.exception;


public class CategoryException extends RuntimeException {

    private String message;

    public CategoryException(String message) {
        super(message);
        this.message=message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
