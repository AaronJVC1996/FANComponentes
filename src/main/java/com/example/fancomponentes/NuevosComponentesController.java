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
    private TextField idComponenteTextField;
    @FXML
    private TextField nombreTextField;

    @FXML
    private TextField stockTextField;

    @FXML
    private TextField precioTextField;

    @FXML
    private TextArea descripcionTextArea;

    @FXML
    private Button btnAgregar;
    @FXML
    private void agregarComponente() {
        String idComponente = idComponenteTextField.getText();
        String nombre = nombreTextField.getText();
        int stock = Integer.parseInt(stockTextField.getText());
        double precio = Double.parseDouble(precioTextField.getText());
        String descripcion = descripcionTextArea.getText();
        try (Connection conexion = DatabaseConnector.getConexion()) {
            String consulta = "INSERT INTO componentes (idcomponente, nombre, stock, precio, descripcion) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement declaracion = conexion.prepareStatement(consulta)) {
                declaracion.setString(1, idComponente);
                declaracion.setString(2, nombre);
                declaracion.setInt(3, stock);
                declaracion.setDouble(4, precio);
                declaracion.setString(5, descripcion);
                declaracion.executeUpdate();

                // Limpiar los campos después de la inserción
                idComponenteTextField.clear();
                nombreTextField.clear();
                stockTextField.clear();
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
