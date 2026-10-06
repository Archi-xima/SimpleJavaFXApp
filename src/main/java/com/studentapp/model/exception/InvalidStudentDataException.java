package com.studentapp.model.exception;

/**
 * Exception thrown when student input validation fails.
 */
public class InvalidStudentDataException extends RuntimeException {
    public InvalidStudentDataException(String message) {
        super(message);
    }
}
