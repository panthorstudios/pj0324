package com.panthorstudios.toolrental.api.exception;

public class InvalidToolCodeException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "Invalid tool code.";

    public InvalidToolCodeException() {
        super(DEFAULT_MESSAGE);
    }
    public InvalidToolCodeException(String message) {
        super(message);
    }

    public InvalidToolCodeException(String message, Throwable cause) {
        super(message, cause);
    }
}