package com.example;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class JavaFXApp extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        URL fxml = Objects.requireNonNull(
                JavaFXApp.class.getResource("file.fxml"),
                "Не найден FXML-файл file.fxml"
        );
        URL css = Objects.requireNonNull(
                JavaFXApp.class.getResource("app.css"),
                "Не найден CSS-файл app.css"
        );
        Parent root = FXMLLoader.load(fxml);
        Scene scene = new Scene(root, 240, 120);
        scene.getStylesheets().add(css.toExternalForm());

        stage.setTitle("JavaFX");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}