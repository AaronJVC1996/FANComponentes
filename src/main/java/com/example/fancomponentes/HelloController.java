package com.example.fancomponentes;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
// Controlador

public class HelloController {
    @FXML
    private Label welcomeText;
    private ImageView logo;
    private Image myImage = new Image(getClass().getResourceAsStream("src/img/logoFanComponentes.jpg"));


    public ImageView getLogo() {
        return logo;
    }

    public void displayImage(){
        logo.setImage(myImage);
    }

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}