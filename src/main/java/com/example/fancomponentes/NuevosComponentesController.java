package com.example.fancomponentes;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javafx.stage.Stage;

public class NuevosComponentesController {

    @FXML
    private TextField nombreTextField;

    @FXML
    private TextField cantidadTextField;

    @FXML
    private TextField precioTextField;

    @FXML
    private TextArea descripcionTextArea;

    @FXML
    private Button btnAgregar;
    @FXML
    private void agregarComponente() {
        String nombre = nombreTextField.getText();
        int cantidad = Integer.parseInt(cantidadTextField.getText());
        double precio = Double.parseDouble(precioTextField.getText());
        String descripcion = descripcionTextArea.getText();
        try (Connection conexion = DatabaseConnector.getConexion()) {
            String consulta = "INSERT INTO componentes (nombre, cantidad, precio, descripcion) VALUES (?, ?, ?, ?)";
            try (PreparedStatement declaracion = conexion.prepareStatement(consulta)) {
                declaracion.setString(1, nombre);
                declaracion.setInt(2, cantidad);
                declaracion.setDouble(3, precio);
                declaracion.setString(4, descripcion);
                declaracion.executeUpdate();

                // Limpiar los campos después de la inserción
                nombreTextField.clear();
                cantidadTextField.clear();
                precioTextField.clear();
                descripcionTextArea.clear();

                // Mostrar mensaje de éxito
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Componente Agregado");
                alert.setContentText("El componente se ha agregado correctamente");
                alert.showAndWait();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Manejar cualquier error que ocurra durante la inserción
        }
        Stage stage = (Stage) btnAgregar.getScene().getWindow(); // Obtener la referencia de la ventana
        stage.close();
    }


}
