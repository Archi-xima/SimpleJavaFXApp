package com.studentapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Main JavaFX Application class for the Student Registration System.
 *
 * Course:      ICT161
 * Lecturer:    Mr. Nyirenda
 * Student:     Achizima Marvelous Mpashi
 * Student ID:  202511543
 * Program:     Data Science
 */
public class StudentApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/studentapp/view/StudentRegistrationView.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root, 1000, 640);

            primaryStage.setTitle("Student Registration System | ICT161 - Mr. Nyirenda");
            primaryStage.setScene(scene);
            primaryStage.setMinWidth(880);
            primaryStage.setMinHeight(520);
            primaryStage.show();

        } catch (IOException e) {
            System.err.println("Failed to load FXML layout: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
