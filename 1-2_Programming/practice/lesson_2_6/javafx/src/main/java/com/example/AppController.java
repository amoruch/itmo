package com.example;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class AppController {

    @FXML
    private Label myLabel;

    @FXML
    private void showGreeting() {
        myLabel.setText("Привет!");
    }
}