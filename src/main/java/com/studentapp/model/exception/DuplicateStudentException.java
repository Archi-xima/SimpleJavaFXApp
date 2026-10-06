package com.studentapp.model.exception;

/**
 * Exception thrown when attempting to register a student with an ID that already exists.
 */
public class DuplicateStudentException extends RuntimeException {
    public DuplicateStudentException(String message) {
        super(message);
    }
}
