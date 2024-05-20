package com.example.fancomponentes;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AniadirStockController {

    @FXML
    private TextField stockTextField;

    @FXML
    private Button btnAgregar;

    private String componenteId;

    public void setComponenteId(String id) {
        this.componenteId = id;
    }
    @FXML
    private void aniadirStock() {
        String cantidadStr = stockTextField.getText();
        int cantidad;
        try {
            cantidad = Integer.parseInt(cantidadStr);
        } catch (NumberFormatException e) {
            mostrarAlerta("Error de formato", "La cantidad debe ser un número entero.");
            return;
        }

        try (Connection conexion = DatabaseConnector.getConexion()) {
            String consulta = "UPDATE componentes SET stock = stock + ? WHERE idcomponente = ?";
            try (PreparedStatement declaracion = conexion.prepareStatement(consulta)) {
                declaracion.setInt(1, cantidad);
                declaracion.setString(2, componenteId);
                declaracion.executeUpdate();
                stockTextField.clear();
                mostrarAlerta("Cantidad Agregada", "La cantidad se ha agregado correctamente");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            mostrarAlerta("Error de base de datos", "No se pudo actualizar el stock.");
        }

        Stage stage = (Stage) btnAgregar.getScene().getWindow();
        stage.close();
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}