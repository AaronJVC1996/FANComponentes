package com.example.fancomponentes;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    // Método para verificar las credenciales del usuario
    @FXML
    protected void onLoginButtonClicked() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        try (Connection conexion = DatabaseConnector.getConexion()) {
            if (verificarCredenciales(conexion, username, password)) {
                System.out.println("Inicio de sesión exitoso");
                // Cargar y mostrar la vista de gestión de almacén
                cargarVistaGestionAlmacen();
            } else {
                System.out.println("Credenciales incorrectas");
                // Aquí puedes mostrar un mensaje de error al usuario
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Manejar cualquier error de conexión o consulta SQL
        }
    }

    // Método para verificar las credenciales del usuario en la base de datos
    private boolean verificarCredenciales(Connection conexion, String username, String password) throws SQLException {
        String sql = "SELECT * FROM ROLES WHERE NOMBRE=? AND CONTRASEÑA=?";
        try (PreparedStatement statement = conexion.prepareStatement(sql)) {
            statement.setString(1, username);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next(); // Si hay resultados, las credenciales son válidas
        }
    }

    // Método para cargar y mostrar la vista de gestión de almacén
    private void cargarVistaGestionAlmacen() {
        try {
            // Cargar la vista de gestión de almacén desde el archivo FXML
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("GestionAlmacenVista.fxml"));
            Parent root = fxmlLoader.load();
            // Obtener el controlador de la vista de gestión de almacén
            GestionAlmacenController controller = fxmlLoader.getController();
            // Crear una nueva escena
            Scene scene = new Scene(root);
            // Obtener la ventana actual y establecer la escena en ella
            Stage stage = (Stage) loginButton.getScene().getWindow();
            stage.setScene(scene);

        } catch (IOException e) {
            e.printStackTrace();
            // Manejar cualquier error de E/S al cargar la vista de gestión de almacén
        }
    }
}