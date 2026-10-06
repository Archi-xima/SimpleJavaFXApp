package com.studentapp.controller;

import com.studentapp.model.Student;
import com.studentapp.model.StudentRepository;
import com.studentapp.model.exception.DuplicateStudentException;
import com.studentapp.model.exception.InvalidStudentDataException;
import com.studentapp.view.StudentConsoleView;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * Controller class managing the flow between View and Model.
 * Handles user actions, business validations, and error routing.
 */
public class StudentController {
    private final StudentRepository repository;
    private final StudentConsoleView view;

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );
    private static final Pattern ID_PATTERN = Pattern.compile("^[A-Za-z0-9]{2,15}$");

    public StudentController(StudentRepository repository, StudentConsoleView view) {
        this.repository = repository;
        this.view = view;
    }

    /**
     * Starts the main application interaction loop.
     */
    public void start() {
        view.displayWelcomeBanner();
        boolean running = true;

        while (running) {
            view.displayMainMenu();
            String choice = view.readUserInput();

            switch (choice) {
                case "1" -> handleRegisterStudent();
                case "2" -> handleViewAllStudents();
                case "3" -> handleSearchStudent();
                case "4" -> {
                    view.displayExitMessage();
                    running = false;
                }
                default -> view.displayErrorMessage("Invalid selection. Please enter a number between 1 and 4.");
            }
        }
    }

    /**
     * Handles the "Register Student" use case with comprehensive error handling.
     */
    public void handleRegisterStudent() {
        view.displayInfoMessage("Starting Student Registration...");

        try {
            // 1. Gather raw inputs from the View
            String rawId = view.promptStudentId();
            String rawName = view.promptStudentName();
            String rawEmail = view.promptStudentEmail();
            String rawCourse = view.promptStudentCourse();

            // 2. Validate input and register student
            Student registeredStudent = registerStudent(rawId, rawName, rawEmail, rawCourse);

            // 3. Notify View of success
            view.displaySuccessMessage("Student successfully registered!");
            view.displayStudentDetails(registeredStudent);

        } catch (DuplicateStudentException e) {
            // Identified Error Scenario 1: Duplicate Student ID
            view.displayErrorMessage("Registration Aborted: " + e.getMessage());
        } catch (InvalidStudentDataException e) {
            // Identified Error Scenario 2: Invalid Input Format / Missing Fields
            view.displayErrorMessage("Validation Error: " + e.getMessage());
        } catch (Exception e) {
            // Unexpected fallback
            view.displayErrorMessage("An unexpected error occurred: " + e.getMessage());
        }
    }

    /**
     * Core business logic for student registration.
     * Validates input fields, checks for duplicates, creates and persists student.
     *
     * @param rawId Raw student ID
     * @param rawName Raw student name
     * @param rawEmail Raw student email
     * @param rawCourse Raw course
     * @return The registered Student object
     * @throws InvalidStudentDataException if validation rules fail
     * @throws DuplicateStudentException if student ID already exists
     */
    public Student registerStudent(String rawId, String rawName, String rawEmail, String rawCourse) {
        // Validation: Clean inputs
        String id = (rawId != null) ? rawId.trim() : "";
        String name = (rawName != null) ? rawName.trim() : "";
        String email = (rawEmail != null) ? rawEmail.trim() : "";
        String course = (rawCourse != null) ? rawCourse.trim() : "";

        // Check empty fields
        if (id.isEmpty()) {
            throw new InvalidStudentDataException("Student ID cannot be blank.");
        }
        if (!ID_PATTERN.matcher(id).matches()) {
            throw new InvalidStudentDataException(
                "Invalid Student ID format. Must be 2-15 alphanumeric characters (no spaces or symbols)."
            );
        }

        if (name.isEmpty()) {
            throw new InvalidStudentDataException("Student name cannot be blank.");
        }
        if (name.length() < 2 || name.length() > 50) {
            throw new InvalidStudentDataException("Student name must be between 2 and 50 characters long.");
        }

        if (email.isEmpty()) {
            throw new InvalidStudentDataException("Student email cannot be blank.");
        }
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new InvalidStudentDataException(
                "Invalid email address format. Example: student@university.edu"
            );
        }

        if (course.isEmpty()) {
            throw new InvalidStudentDataException("Course/Major cannot be blank.");
        }

        // Check duplicate constraint
        if (repository.existsById(id)) {
            throw new DuplicateStudentException(
                "Student ID '" + id + "' is already registered in the system."
            );
        }

        // Create Model instance and persist
        Student student = new Student(id, name, email, course);
        repository.save(student);

        return student;
    }

    /**
     * Handles displaying all students.
     */
    public void handleViewAllStudents() {
        List<Student> students = repository.findAll();
        view.displayAllStudents(students);
    }

    /**
     * Handles searching for a student by ID.
     */
    public void handleSearchStudent() {
        String id = view.promptStudentId();
        if (id == null || id.trim().isEmpty()) {
            view.displayErrorMessage("Student ID cannot be empty.");
            return;
        }

        Optional<Student> studentOpt = repository.findById(id.trim());
        if (studentOpt.isPresent()) {
            view.displayStudentDetails(studentOpt.get());
        } else {
            view.displayErrorMessage("No student found with ID: " + id.trim());
        }
    }
}
