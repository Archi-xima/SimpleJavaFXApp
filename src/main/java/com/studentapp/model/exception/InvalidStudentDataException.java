package com.studentapp.model.exception;

/**
 * Exception thrown when student input validation fails.
 *
 * Course:      ICT161 (Mr. Nyirenda)
 * Student:     Achizima Marvelous Mpashi (202511543)
 * Program:     Data Science
 */
public class InvalidStudentDataException extends RuntimeException {
    public InvalidStudentDataException(String message) {
        super(message);
    }
}
