package com.example.fancomponentes;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.util.Duration;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class pantallaLoginCorrectoController  implements Initializable {

    @FXML
    private Label idNombreApp;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

   /* private void cargarVistaGestionAlmacen() {
        try {
            // Cargar la vista de gestión de almacén desde el archivo FXML
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("GestionAlmacenVista.fxml"));
            Parent root = fxmlLoader.load();

            // Crear una nueva escena y establecerla en la actual
            Scene scene = new Scene(root);
            Stage stage = (Stage) idNombreApp.getScene().getWindow();
            stage.setScene(scene);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/
}
