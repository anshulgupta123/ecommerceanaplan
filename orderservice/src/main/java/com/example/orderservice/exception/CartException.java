package com.example.orderservice.exception;

public class CartException extends RuntimeException {

    private String message;

    public CartException(String message) {
        super(message);
        this.message=message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}