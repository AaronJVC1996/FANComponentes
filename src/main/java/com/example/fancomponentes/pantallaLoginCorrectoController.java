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
        PauseTransition delay = new PauseTransition(Duration.seconds(2));
        delay.setOnFinished(event -> cargarVistaGestionAlmacen());
        delay.play();
    }

    private void cargarVistaGestionAlmacen() {
        try {
            // Cargar la vista de gestión de almacén desde el archivo FXML
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("GestionAlmacenVista.fxml"));
            Parent root = fxmlLoader.load();

            // Obtener el controlador de la vista de gestión de almacén
            GestionAlmacenController controller = fxmlLoader.getController();

            // Crear una nueva escena
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);

            Stage stageActual = (Stage) idNombreApp.getScene().getWindow();

            stage.show();
            stageActual.close();

        } catch (IOException e) {
            e.printStackTrace();
            // Manejar cualquier error de E/S al cargar la vista de gestión de almacén
        }
    }

}
