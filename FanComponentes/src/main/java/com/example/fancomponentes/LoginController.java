package com.example.fancomponentes;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;

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

        // Conectar a la base de datos
        try (Connection conexion = DatabaseConnector.getConexion()) {
            // Verificar las credenciales del usuario en la base de datos
            if (verificarCredenciales(conexion, username, password)) {
                System.out.println("Inicio de sesión exitoso");
                // Aquí puedes abrir una nueva ventana o realizar alguna acción después de iniciar sesión
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
}