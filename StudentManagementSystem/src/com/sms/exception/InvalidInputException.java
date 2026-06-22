package com.sms.exception;

/**
 * Custom exception thrown when user input is invalid.
 * This extends Exception to make it a checked exception.
 */
public class InvalidInputException extends Exception {

    /**
     * Constructor with field name and reason
     * @param fieldName The field that has invalid input
     * @param reason The reason why the input is invalid
     */
    public InvalidInputException(String fieldName, String reason) {
        super("Invalid input for '" + fieldName + "': " + reason);
    }

    /**
     * Constructor with custom message
     * @param message Custom error message
     */
    public InvalidInputException(String message) {
        super(message);
    }

    /**
     * Constructor with cause
     * @param message Custom error message
     * @param cause The underlying cause
     */
    public InvalidInputException(String message, Throwable cause) {
        super(message, cause);
    }
}