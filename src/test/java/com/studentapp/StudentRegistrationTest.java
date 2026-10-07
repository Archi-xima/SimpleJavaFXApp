package com.studentapp;

import com.studentapp.controller.StudentRegistrationController;
import com.studentapp.model.Student;
import com.studentapp.model.StudentRepository;
import com.studentapp.model.exception.DuplicateStudentException;
import com.studentapp.model.exception.InvalidStudentDataException;

/**
 * Self-contained Verification Test Suite for Student Registration & Error Handling.
 *
 * Course:      ICT161
 * Lecturer:    Mr. Nyirenda
 * Student:     Achizima Marvelous Mpashi
 * Student ID:  202511543
 * Program:     Data Science
 */
public class StudentRegistrationTest {
    public static void main(String[] args) {
        System.out.println("Running ICT161 Student Registration automated tests...\n");

        testSuccessfulRegistration();
        testDuplicateStudentError();
        testInvalidEmailError();
        testBlankFieldsError();

        System.out.println("\nAll tests passed successfully!");
    }

    private static void testSuccessfulRegistration() {
        StudentRegistrationController controller = new StudentRegistrationController();

        Student student = controller.registerStudent("202511543", "Achizima Marvelous Mpashi", "mpashiachie@gmail.com", "Data Science");
        assert student != null : "Student should not be null";
        assert student.getId().equals("202511543") : "Student ID should match";
        System.out.println("[PASS] testSuccessfulRegistration (Registered Achizima Marvelous Mpashi)");
    }

    private static void testDuplicateStudentError() {
        StudentRegistrationController controller = new StudentRegistrationController();

        controller.registerStudent("202511543", "Achizima Marvelous Mpashi", "mpashiachie@gmail.com", "Data Science");

        try {
            // Attempt duplicate registration with the same Student ID
            controller.registerStudent("202511543", "Duplicate Student", "duplicate@gmail.com", "Data Science");
            throw new AssertionError("Expected DuplicateStudentException was not thrown");
        } catch (DuplicateStudentException e) {
            System.out.println("[PASS] testDuplicateStudentError: " + e.getMessage());
        }
    }

    private static void testInvalidEmailError() {
        StudentRegistrationController controller = new StudentRegistrationController();

        try {
            controller.registerStudent("STU102", "Jane Doe", "invalid-email-address", "Data Science");
            throw new AssertionError("Expected InvalidStudentDataException was not thrown for invalid email");
        } catch (InvalidStudentDataException e) {
            System.out.println("[PASS] testInvalidEmailError: " + e.getMessage());
        }
    }

    private static void testBlankFieldsError() {
        StudentRegistrationController controller = new StudentRegistrationController();

        try {
            controller.registerStudent("", "John Doe", "john@university.edu", "Data Science");
            throw new AssertionError("Expected InvalidStudentDataException was not thrown for empty ID");
        } catch (InvalidStudentDataException e) {
            System.out.println("[PASS] testBlankFieldsError: " + e.getMessage());
        }
    }
}
