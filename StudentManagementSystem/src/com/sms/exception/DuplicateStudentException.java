package com.sms.exception;

/**
 * Custom exception thrown when trying to add a student with a duplicate ID.
 * This extends RuntimeException to make it an unchecked exception.
 */
public class DuplicateStudentException extends RuntimeException {

    /**
     * Constructor with student ID
     * @param studentId The duplicate ID
     */
    public DuplicateStudentException(String studentId) {
        super("Student with ID '" + studentId + "' already exists in the system.");
    }

    /**
     * Constructor with custom message
     * @param message Custom error message
     * @param studentId The duplicate ID
     */
    public DuplicateStudentException(String message, String studentId) {
        super(message + " (ID: " + studentId + ")");
    }
}