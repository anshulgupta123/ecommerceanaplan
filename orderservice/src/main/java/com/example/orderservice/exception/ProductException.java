package com.example.orderservice.exception;

public class ProductException extends RuntimeException {

    private String message;

    public ProductException(String message) {
        super(message);
        this.message=message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
