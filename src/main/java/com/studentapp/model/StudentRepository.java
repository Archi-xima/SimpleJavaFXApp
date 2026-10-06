package com.studentapp.model;

import com.studentapp.model.exception.DuplicateStudentException;

import java.util.*;

/**
 * Repository for managing Student data persistence.
 *
 * Course:      ICT161
 * Lecturer:    Mr. Nyirenda
 * Student:     Achizima Marvelous Mpashi (202511543)
 * Program:     Data Science
 */
public class StudentRepository {
    // LinkedHashMap preserves insertion order for display
    private final Map<String, Student> studentStore = new LinkedHashMap<>();

    /**
     * Checks if a student with the given ID already exists.
     *
     * @param id The student ID to check
     * @return true if ID already exists, false otherwise
     */
    public boolean existsById(String id) {
        if (id == null) return false;
        return studentStore.containsKey(id.trim().toUpperCase());
    }

    /**
     * Saves a student in the repository.
     * Throws DuplicateStudentException if the student ID already exists.
     *
     * @param student The student to save
     * @throws DuplicateStudentException if student with same ID already exists
     */
    public void save(Student student) {
        if (student == null) {
            throw new IllegalArgumentException("Student cannot be null.");
        }
        String normalizedId = student.getId().trim().toUpperCase();
        if (studentStore.containsKey(normalizedId)) {
            throw new DuplicateStudentException(
                "Registration failed: Student with ID '" + student.getId() + "' already exists in the system."
            );
        }
        studentStore.put(normalizedId, student);
    }

    /**
     * Finds a student by ID.
     *
     * @param id The student ID
     * @return Optional containing the student if found
     */
    public Optional<Student> findById(String id) {
        if (id == null) return Optional.empty();
        return Optional.ofNullable(studentStore.get(id.trim().toUpperCase()));
    }

    /**
     * Returns a list of all registered students.
     *
     * @return List of all students
     */
    public List<Student> findAll() {
        return new ArrayList<>(studentStore.values());
    }

    /**
     * Returns total count of registered students.
     *
     * @return total student count
     */
    public int count() {
        return studentStore.size();
    }
}
