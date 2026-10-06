package com.studentapp;

import javafx.application.Application;

/**
 * Main Launcher Class.
 * Acts as an entry point that delegates to the JavaFX Application lifecycle.
 * This pattern prevents the common "JavaFX runtime components are missing"
 * error when launching inside IntelliJ IDEA or from standard JARs.
 *
 * Course:      ICT161
 * Lecturer:    Mr. Nyirenda
 * Student:     Achizima Marvelous Mpashi
 * Student ID:  202511543
 * Program:     Data Science
 */
public class Main {
    public static void main(String[] args) {
        Application.launch(StudentApp.class, args);
    }
}
