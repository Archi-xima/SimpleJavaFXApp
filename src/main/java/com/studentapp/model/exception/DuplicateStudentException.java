package com.studentapp.model.exception;

/**
 * Exception thrown when attempting to register a student with an ID that already exists.
 *
 * Course:      ICT161 (Mr. Nyirenda)
 * Student:     Achizima Marvelous Mpashi (202511543)
 * Program:     Data Science
 */
public class DuplicateStudentException extends RuntimeException {
    public DuplicateStudentException(String message) {
        super(message);
    }
}
