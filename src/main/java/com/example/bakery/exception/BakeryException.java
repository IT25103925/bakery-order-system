package com.example.bakery.exception;

/**
 * Custom exception for the bakery system.
 * Lecture 06: Custom Exception Handling
 */
public class BakeryException extends RuntimeException {
    private final String errorCode;

    public BakeryException(String message) {
        super(message);
        this.errorCode = "BAKERY_ERROR";
    }

    public BakeryException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
