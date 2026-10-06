package com.studentapp.controller;

import com.studentapp.model.Student;
import com.studentapp.model.StudentRepository;
import com.studentapp.model.exception.DuplicateStudentException;
import com.studentapp.model.exception.InvalidStudentDataException;
import com.studentapp.view.StudentGuiView;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * Controller connecting the Swing GUI View with the Student Model/Repository.
 * Enforces business logic and routes error messages.
 */
public class StudentGuiController {
    private final StudentRepository repository;
    private final StudentGuiView view;

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );
    private static final Pattern ID_PATTERN = Pattern.compile("^[A-Za-z0-9]{2,15}$");

    public StudentGuiController(StudentRepository repository, StudentGuiView view) {
        this.repository = repository;
        this.view = view;
        initController();
    }

    private void initController() {
        // Wire View event listeners to Controller actions
        view.addRegisterListener(e -> handleRegister());
        view.addClearListener(e -> handleClear());
        view.addSearchListener(e -> handleSearch());
        view.addRefreshListener(e -> handleRefresh());

        // Initial table load
        refreshTable();
    }

    public void handleRegister() {
        String rawId = view.getIdInput();
        String rawName = view.getNameInput();
        String rawEmail = view.getEmailInput();
        String rawCourse = view.getCourseInput();

        try {
            Student student = registerStudent(rawId, rawName, rawEmail, rawCourse);
            refreshTable();
            view.clearFormFields();
            view.showSuccess("Student '" + student.getName() + "' (ID: " + student.getId() + ") successfully registered!");
        } catch (DuplicateStudentException e) {
            view.showError("Registration Error: " + e.getMessage());
        } catch (InvalidStudentDataException e) {
            view.showError("Validation Error: " + e.getMessage());
        } catch (Exception e) {
            view.showError("Unexpected error occurred: " + e.getMessage());
        }
    }

    public Student registerStudent(String rawId, String rawName, String rawEmail, String rawCourse) {
        String id = (rawId != null) ? rawId.trim() : "";
        String name = (rawName != null) ? rawName.trim() : "";
        String email = (rawEmail != null) ? rawEmail.trim() : "";
        String course = (rawCourse != null) ? rawCourse.trim() : "";

        if (id.isEmpty()) {
            throw new InvalidStudentDataException("Student ID cannot be blank.");
        }
        if (!ID_PATTERN.matcher(id).matches()) {
            throw new InvalidStudentDataException(
                "Invalid Student ID. Must be 2-15 alphanumeric characters."
            );
        }

        if (name.isEmpty()) {
            throw new InvalidStudentDataException("Student name cannot be blank.");
        }
        if (name.length() < 2 || name.length() > 50) {
            throw new InvalidStudentDataException("Student name must be between 2 and 50 characters.");
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

        if (repository.existsById(id)) {
            throw new DuplicateStudentException(
                "Student ID '" + id + "' is already registered in the system."
            );
        }

        Student student = new Student(id, name, email, course);
        repository.save(student);
        return student;
    }

    public void handleClear() {
        view.clearFormFields();
        view.showInfo("Form cleared.");
    }

    public void handleSearch() {
        String id = view.getSearchInput();
        if (id == null || id.trim().isEmpty()) {
            view.showError("Please enter a Student ID to search.");
            return;
        }

        Optional<Student> studentOpt = repository.findById(id.trim());
        if (studentOpt.isPresent()) {
            view.updateStudentTable(List.of(studentOpt.get()));
            view.showInfo("Found student with ID: " + id.trim());
        } else {
            view.showError("No student found with ID: " + id.trim());
        }
    }

    public void handleRefresh() {
        refreshTable();
        view.showInfo("Displaying all registered students.");
    }

    private void refreshTable() {
        view.updateStudentTable(repository.findAll());
    }

    public void display() {
        view.setVisible(true);
    }
}
