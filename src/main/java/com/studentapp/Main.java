package com.studentapp;

import com.studentapp.controller.StudentController;
import com.studentapp.controller.StudentGuiController;
import com.studentapp.model.StudentRepository;
import com.studentapp.view.StudentConsoleView;
import com.studentapp.view.StudentGuiView;

import javax.swing.*;
import java.awt.*;

/**
 * Application Entry Point.
 * Launches the modern Swing Graphical User Interface (GUI) by default,
 * with automatic fallback to console if running in a headless environment
 * or when launched with the '--console' flag.
 */
public class Main {
    public static void main(String[] args) {
        // Shared Model repository
        StudentRepository repository = new StudentRepository();

        // Optional console mode flag
        boolean forceConsole = args.length > 0 && args[0].equalsIgnoreCase("--console");

        if (!forceConsole && !GraphicsEnvironment.isHeadless()) {
            // Launch Modern Swing GUI
            SwingUtilities.invokeLater(() -> {
                StudentGuiView guiView = new StudentGuiView();
                StudentGuiController guiController = new StudentGuiController(repository, guiView);
                guiController.display();
            });
        } else {
            // Launch Console Interface
            StudentConsoleView consoleView = new StudentConsoleView();
            StudentController consoleController = new StudentController(repository, consoleView);
            consoleController.start();
        }
    }
}
