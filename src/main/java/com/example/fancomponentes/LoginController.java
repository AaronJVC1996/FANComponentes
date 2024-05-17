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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    @FXML
    private ImageView idImagenLogo;

    @FXML
    private void initialize() {
        idImagenLogo.setPreserveRatio(true);

        idImagenLogo.setFitWidth(200);
        idImagenLogo.setFitHeight(300);
    }


    // Método para verificar las credenciales del usuario
    @FXML
    protected void onLoginButtonClicked() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        try (Connection conexion = DatabaseConnector.getConexion()) {
            if (verificarCredenciales(username, password)) {
                System.out.println("Inicio de sesión exitoso");
                //Si inicio es correcto.
                /* Stage stage =(Stage) root.getScene().getWindow();
                try{
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/path/to/pantallaLoginCorrecto.fxml"));
                    Parent root = loader.load();
                }*/


                // Cargar y mostrar la vista de gestión de almacén
                cargarVistaGestionAlmacen();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("Credenciales incorrectas. " +
                        "El usuario con el que está intentando logearse no tiene permisos para acceder a la aplicación, " +
                        "contacte con el CAU para poder solucionarlo");
                alert.setHeaderText("ERROR DE LOGIN");
                alert.showAndWait();
               // System.out.println("Credenciales incorrectas");
                // Aquí puedes mostrar un mensaje de error al usuario
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Manejar cualquier error de conexión o consulta SQL
        }
    }

    // Método para verificar las credenciales del usuario en la base de datos
    private boolean verificarCredenciales(String idRol, String password) throws SQLException {
        Connection conexion = DatabaseConnector.getConexion();
        String sql = "SELECT * FROM ROLES WHERE IDROL=? AND CONTRASEÑA=?";
        try (PreparedStatement statement = conexion.prepareStatement(sql)) {
            statement.setString(1, idRol);
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