package com.example.hellofx;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class HelloJavaFX extends Application {

    @Override
    public void start(Stage stage) {

        Label label = new Label("Welcome, Achizima Marvelous Mpashi!");

        Button startButton = new Button("Start");
        Button resetButton = new Button("Reset");

        startButton.setOnAction(e -> {
            label.setText("JavaFX Started!");
        });

        resetButton.setOnAction(e -> {
            label.setText("Welcome, Achizima Marvelous Mpashi!");
        });

        VBox root = new VBox(10);
        root.getChildren().addAll(label, startButton, resetButton);

        Scene scene = new Scene(root, 400, 200);

        stage.setTitle("JavaFX Lab - 202511543");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}