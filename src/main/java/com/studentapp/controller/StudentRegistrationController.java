package com.studentapp.controller;

import com.studentapp.model.Student;
import com.studentapp.model.StudentRepository;
import com.studentapp.model.exception.DuplicateStudentException;
import com.studentapp.model.exception.InvalidStudentDataException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

/**
 * JavaFX FXML Controller for the Student Registration View.
 * Implements Controller logic adhering to the MVC pattern.
 *
 * Course:      ICT161
 * Lecturer:    Mr. Nyirenda
 * Student:     Achizima Marvelous Mpashi
 * Student ID:  202511543
 * Program:     Data Science
 */
public class StudentRegistrationController implements Initializable {

    // Model
    private final StudentRepository repository = new StudentRepository();

    // Observable data list for JavaFX TableView
    private final ObservableList<Student> studentObservableList = FXCollections.observableArrayList();

    // Validation patterns
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );
    private static final Pattern ID_PATTERN = Pattern.compile("^[A-Za-z0-9]{2,15}$");

    // FXML Form Inputs
    @FXML private TextField idField;
    @FXML private TextField nameField;
    @FXML private TextField emailField;
    @FXML private TextField courseField;

    // FXML Table & Columns
    @FXML private TableView<Student> studentTable;
    @FXML private TableColumn<Student, String> idColumn;
    @FXML private TableColumn<Student, String> nameColumn;
    @FXML private TableColumn<Student, String> emailColumn;
    @FXML private TableColumn<Student, String> courseColumn;

    // FXML Toolbar & Status
    @FXML private TextField searchField;
    @FXML private Label totalStudentsLabel;
    @FXML private Label formNotificationLabel;
    @FXML private Label statusLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Configure TableView columns with Student property mappings
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        courseColumn.setCellValueFactory(new PropertyValueFactory<>("course"));

        // Bind TableView to observable list
        studentTable.setItems(studentObservableList);

        // Pre-populate with a sample Data Science student to demonstrate UI
        loadInitialData();
    }

    private void loadInitialData() {
        try {
            Student sampleStudent = new Student("202511543", "Achizima Marvelous Mpashi", "achizima@unza.zm", "Data Science");
            repository.save(sampleStudent);
            studentObservableList.add(sampleStudent);
            updateStudentCount();
            setStatus("Ready. Initialized with sample Data Science student record.");
        } catch (Exception ignored) {}
    }

    /**
     * Handles the "Register Student" action.
     * Enforces validations and catches custom domain exceptions.
     */
    @FXML
    public void handleRegisterStudent() {
        String rawId = idField.getText();
        String rawName = nameField.getText();
        String rawEmail = emailField.getText();
        String rawCourse = courseField.getText();

        try {
            // Business Validation & Persistence via Model
            Student student = registerStudent(rawId, rawName, rawEmail, rawCourse);

            // Update View
            studentObservableList.add(student);
            updateStudentCount();
            handleClearForm();

            showSuccessNotification("Student '" + student.getName() + "' (ID: " + student.getId() + ") successfully registered!");
            setStatus("Student registered: " + student.getId());

            showAlert(Alert.AlertType.INFORMATION, "Registration Successful",
                    "Student Registered Successfully!",
                    "Student ID: " + student.getId() + "\n" +
                    "Name: " + student.getName() + "\n" +
                    "Email: " + student.getEmail() + "\n" +
                    "Program/Course: " + student.getCourse());

        } catch (DuplicateStudentException e) {
            // Identified Error Scenario 1: Duplicate Student ID
            showErrorNotification("Registration Failed: " + e.getMessage());
            setStatus("Error: Duplicate Student ID attempted.");
            showAlert(Alert.AlertType.ERROR, "Duplicate Student ID Error",
                    "Registration Failed: Student ID Already Exists", e.getMessage());

        } catch (InvalidStudentDataException e) {
            // Identified Error Scenario 2: Validation Failure
            showErrorNotification("Validation Error: " + e.getMessage());
            setStatus("Error: Invalid form input.");
            showAlert(Alert.AlertType.WARNING, "Input Validation Error",
                    "Validation Rule Violation", e.getMessage());

        } catch (Exception e) {
            showErrorNotification("Unexpected error: " + e.getMessage());
            setStatus("Unexpected error occurred.");
            showAlert(Alert.AlertType.ERROR, "System Error",
                    "An unexpected error occurred", e.getMessage());
        }
    }

    /**
     * Business validation logic for student registration.
     */
    public Student registerStudent(String rawId, String rawName, String rawEmail, String rawCourse) {
        String id = (rawId != null) ? rawId.trim() : "";
        String name = (rawName != null) ? rawName.trim() : "";
        String email = (rawEmail != null) ? rawEmail.trim() : "";
        String course = (rawCourse != null) ? rawCourse.trim() : "";

        // Check empty fields
        if (id.isEmpty()) {
            throw new InvalidStudentDataException("Student ID cannot be blank.");
        }
        if (!ID_PATTERN.matcher(id).matches()) {
            throw new InvalidStudentDataException("Student ID must be 2 to 15 alphanumeric characters (no spaces or special symbols).");
        }

        if (name.isEmpty()) {
            throw new InvalidStudentDataException("Full Name cannot be blank.");
        }
        if (name.length() < 2 || name.length() > 50) {
            throw new InvalidStudentDataException("Full Name must be between 2 and 50 characters.");
        }

        if (email.isEmpty()) {
            throw new InvalidStudentDataException("Email Address cannot be blank.");
        }
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new InvalidStudentDataException("Invalid email format. Expected format: student@university.edu");
        }

        if (course.isEmpty()) {
            throw new InvalidStudentDataException("Course/Major cannot be blank.");
        }

        // Duplicate Check
        if (repository.existsById(id)) {
            throw new DuplicateStudentException("Student ID '" + id + "' is already registered in the system.");
        }

        // Model Creation and Persistence
        Student student = new Student(id, name, email, course);
        repository.save(student);
        return student;
    }

    /**
     * Clears all registration input fields.
     */
    @FXML
    public void handleClearForm() {
        idField.clear();
        nameField.clear();
        emailField.clear();
        courseField.clear();
        idField.requestFocus();
    }

    /**
     * Filters the TableView by student ID.
     */
    @FXML
    public void handleSearchStudent() {
        String searchId = searchField.getText();
        if (searchId == null || searchId.trim().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Search Warning", "Empty Search Query", "Please enter a Student ID to search.");
            return;
        }

        Optional<Student> studentOpt = repository.findById(searchId.trim());
        if (studentOpt.isPresent()) {
            studentObservableList.setAll(List.of(studentOpt.get()));
            setStatus("Found student matching ID: " + searchId.trim());
        } else {
            showAlert(Alert.AlertType.INFORMATION, "Search Result", "Student Not Found", "No student found with ID: " + searchId.trim());
            setStatus("No student found with ID: " + searchId.trim());
        }
    }

    /**
     * Refreshes the TableView to show all registered students.
     */
    @FXML
    public void handleRefreshTable() {
        searchField.clear();
        studentObservableList.setAll(repository.findAll());
        updateStudentCount();
        setStatus("Displaying all registered students.");
    }

    private void updateStudentCount() {
        totalStudentsLabel.setText("Total: " + repository.count() + " Students");
    }

    private void setStatus(String message) {
        if (statusLabel != null) {
            statusLabel.setText(message);
        }
    }

    private void showSuccessNotification(String message) {
        if (formNotificationLabel != null) {
            formNotificationLabel.getStyleClass().removeAll("notification-error", "notification-success");
            formNotificationLabel.getStyleClass().add("notification-success");
            formNotificationLabel.setText("✓ " + message);
        }
    }

    private void showErrorNotification(String message) {
        if (formNotificationLabel != null) {
            formNotificationLabel.getStyleClass().removeAll("notification-error", "notification-success");
            formNotificationLabel.getStyleClass().add("notification-error");
            formNotificationLabel.setText("⚠ " + message);
        }
    }

    private void showAlert(Alert.AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
