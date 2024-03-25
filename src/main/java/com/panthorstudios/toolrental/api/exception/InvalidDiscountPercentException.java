package com.panthorstudios.toolrental.api.exception;

public class InvalidDiscountPercentException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "Invalid discount percent.";

    public InvalidDiscountPercentException() {
        super(DEFAULT_MESSAGE);
    }

    public InvalidDiscountPercentException(String message) {
        super(message);
    }

    public InvalidDiscountPercentException(String message, Throwable cause) {
        super(message, cause);
    }
}