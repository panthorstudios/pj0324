package com.panthorstudios.toolrental.api.exception;

public class InvalidCheckoutDateException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "Invalid checkout date.";

    public InvalidCheckoutDateException() {
        super(DEFAULT_MESSAGE);
    }

    public InvalidCheckoutDateException(String message) {
        super(message);
    }

    public InvalidCheckoutDateException(String message, Throwable cause) {
        super(message, cause);
    }
}