package com.sms.exception;

/**
 * Custom exception thrown when a student is not found in the system.
 * This extends RuntimeException to make it an unchecked exception.
 */
public class StudentNotFoundException extends RuntimeException {

    /**
     * Constructor with student ID
     * @param studentId The ID that was searched for
     */
    public StudentNotFoundException(String studentId) {
        super("Student with ID '" + studentId + "' was not found in the system.");
    }

    /**
     * Constructor with custom message
     * @param message Custom error message
     */
    public StudentNotFoundException(String message, String studentId) {
        super(message + " (ID: " + studentId + ")");
    }

    /**
     * Constructor with cause
     * @param message Custom error message
     * @param cause The underlying cause
     */
    public StudentNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}