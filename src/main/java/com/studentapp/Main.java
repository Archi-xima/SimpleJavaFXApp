package com.studentapp;

import com.studentapp.controller.StudentController;
import com.studentapp.model.StudentRepository;
import com.studentapp.view.StudentConsoleView;

/**
 * Application Entry Point.
 * Sets up Dependency Injection by instantiating Model, View, and Controller.
 */
public class Main {
    public static void main(String[] args) {
        // Instantiate Model (Repository)
        StudentRepository repository = new StudentRepository();

        // Instantiate View
        StudentConsoleView view = new StudentConsoleView();

        // Instantiate Controller with injected dependencies
        StudentController controller = new StudentController(repository, view);

        // Start the application
        controller.start();
    }
}
