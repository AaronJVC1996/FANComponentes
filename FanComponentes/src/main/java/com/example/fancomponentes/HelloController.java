package com.example.fancomponentes;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
// Controlador

public class HelloController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}