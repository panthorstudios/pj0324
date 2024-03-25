package com.panthorstudios.toolrental.api.exception;

public class InvalidRentalDaysException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "Invalid rental days.";

    public InvalidRentalDaysException() {
        super(DEFAULT_MESSAGE);
    }
    public InvalidRentalDaysException(String message) {
        super(message);
    }

    public InvalidRentalDaysException(String message, Throwable cause) {
        super(message, cause);
    }
}