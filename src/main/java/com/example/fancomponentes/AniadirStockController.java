package com.example.fancomponentes;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AniadirStockController {

    @FXML
    private TextField nombreTextField;
    @FXML
    private TextField cantidadTextField;

    @FXML
    private Button btnAgregar;
    @FXML
    private void aniadirStock() {
        int cantidad = Integer.parseInt(cantidadTextField.getText());
        try (Connection conexion = DatabaseConnector.getConexion()) {
            // Consulta SQL para actualizar el stock
            String consulta = "UPDATE COMPONENTES SET STOCK = STOCK + ? WHERE NOMBRE = ?";
            try (PreparedStatement declaracion = conexion.prepareStatement(consulta)) {
                // Establecer los parámetros en la consulta SQL
                declaracion.setInt(1, cantidad); // Sumar la cantidad ingresada al stock existente
                declaracion.setString(2, nombreTextField.getText()); // Obtener el nombre del componente del campo de texto
                declaracion.executeUpdate(); // Ejecutar la consulta SQL
                // Limpiar los campos después de la inserción
                cantidadTextField.clear();
                // Mostrar mensaje de que va correctamente
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Cantidad Agregada");
                alert.setContentText("La cantidad se ha agregado correctamente");
                alert.showAndWait();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Manejar cualquier error que ocurra durante la inserción
        }
        // Cerrar la ventana de añadir stock después de la inserción
        Stage stage = (Stage) btnAgregar.getScene().getWindow(); // Obtener la referencia de la ventana
        stage.close();
    }
}
