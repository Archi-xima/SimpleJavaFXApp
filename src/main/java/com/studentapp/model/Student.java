package com.studentapp.model;

import java.util.Objects;

/**
 * Model class representing a Student entity.
 */
public class Student {
    private final String id;
    private final String name;
    private final String email;
    private final String course;

    public Student(String id, String name, String email, String course) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.course = course;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getCourse() {
        return course;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return Objects.equals(id, student.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("Student [ID: %s | Name: %s | Email: %s | Course: %s]",
                id, name, email, course);
    }
}
