package com.example.fancomponentes;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
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

    private char letraUsuario;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        PauseTransition delay = new PauseTransition(Duration.seconds(2));
        delay.setOnFinished(event -> mostrarVistasSegunTipoUsuario());
        delay.play();
    }

    public void setLetraUsuario(char letraUsuario) {
        this.letraUsuario = letraUsuario;
    }

    // Condicion de mostras qué vistas segun el tipo de rol de usuario
    private void mostrarVistasSegunTipoUsuario(){
        if (letraUsuario == 'E'){
            cargarVistaGestionAlmacen();
        } else if (letraUsuario == 'P'){
            cargarVistaGestionDispositivo();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("La letra de ID de usuario no reconocida");
            alert.setHeaderText("Id no reconocido");
            alert.showAndWait();
        }
    }

    // Método para cargar las vistas relacionadas con gestion de dispositivos, acceso exclusivo de Rol de Proveedor.
    private void cargarVistaGestionDispositivo(){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("GestionDispositivos.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);

            Stage stageActual = (Stage) idNombreApp.getScene().getWindow();
            stage.show();
            stageActual.close();
        } catch (IOException e){
            e.printStackTrace();
        }
    }


    // Método para cargar vistas relacionadas con la gestion de almacen, acceso exclusivo de Rol de Empleado
    private void cargarVistaGestionAlmacen() {
        try {
            // Cargar la vista de gestión de almacén desde el archivo FXML
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("GestionAlmacenVista.fxml"));
            Parent root = fxmlLoader.load();

            // Obtener el controlador de la vista de gestión de almacén
           // GestionAlmacenController controller = fxmlLoader.getController();

            // Crear una nueva escena
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);

            Stage stageActual = (Stage) idNombreApp.getScene().getWindow();
            stage.show();
            stageActual.close();

        } catch (IOException e) {
            e.printStackTrace();

        }


    }

}
