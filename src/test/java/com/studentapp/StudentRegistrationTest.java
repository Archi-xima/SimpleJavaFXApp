package com.studentapp;

import com.studentapp.controller.StudentController;
import com.studentapp.model.Student;
import com.studentapp.model.StudentRepository;
import com.studentapp.model.exception.DuplicateStudentException;
import com.studentapp.model.exception.InvalidStudentDataException;
import com.studentapp.view.StudentConsoleView;

/**
 * Self-contained Verification Test Suite for Student Registration & Error Handling.
 */
public class StudentRegistrationTest {
    public static void main(String[] args) {
        System.out.println("Running Student Registration automated tests...\n");

        testSuccessfulRegistration();
        testDuplicateStudentError();
        testInvalidEmailError();
        testBlankFieldsError();

        System.out.println("\nAll tests passed successfully!");
    }

    private static void testSuccessfulRegistration() {
        StudentRepository repo = new StudentRepository();
        StudentController controller = new StudentController(repo, new StudentConsoleView());

        Student student = controller.registerStudent("STU101", "Alice Smith", "alice@university.edu", "Computer Science");
        assert student != null : "Student should not be null";
        assert repo.existsById("STU101") : "Repository should contain STU101";
        System.out.println("[PASS] testSuccessfulRegistration");
    }

    private static void testDuplicateStudentError() {
        StudentRepository repo = new StudentRepository();
        StudentController controller = new StudentController(repo, new StudentConsoleView());

        controller.registerStudent("STU101", "Alice Smith", "alice@university.edu", "Computer Science");

        try {
            controller.registerStudent("STU101", "Bob Jones", "bob@university.edu", "Mathematics");
            throw new AssertionError("Expected DuplicateStudentException was not thrown");
        } catch (DuplicateStudentException e) {
            System.out.println("[PASS] testDuplicateStudentError: " + e.getMessage());
        }
    }

    private static void testInvalidEmailError() {
        StudentRepository repo = new StudentRepository();
        StudentController controller = new StudentController(repo, new StudentConsoleView());

        try {
            controller.registerStudent("STU102", "Charlie Brown", "invalid-email-address", "Physics");
            throw new AssertionError("Expected InvalidStudentDataException was not thrown for invalid email");
        } catch (InvalidStudentDataException e) {
            System.out.println("[PASS] testInvalidEmailError: " + e.getMessage());
        }
    }

    private static void testBlankFieldsError() {
        StudentRepository repo = new StudentRepository();
        StudentController controller = new StudentController(repo, new StudentConsoleView());

        try {
            controller.registerStudent("", "David Miller", "david@university.edu", "Biology");
            throw new AssertionError("Expected InvalidStudentDataException was not thrown for empty ID");
        } catch (InvalidStudentDataException e) {
            System.out.println("[PASS] testBlankFieldsError: " + e.getMessage());
        }
    }
}
