package com.example.fancomponentes;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
//Modelo
public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
// Conectar a la base de datos MySQL
        Connection conn = DatabaseConnector.conectar();

        // Si la conexión es exitosa, cargar la vista y mostrarla en la ventana
        if (conn != null) {
            try {
                // Crear una sentencia SQL
                Statement statement = conn.createStatement();

                // Ejecutar la consulta SQL
                ResultSet resultSet = statement.executeQuery("SELECT * FROM roles");

                // Iterar sobre los resultados y mostrarlos en la consola
                while (resultSet.next()) {
                    // Suponiendo que tienes columnas llamadas "idrol", "nombre" y "contraseña"
                    String id = resultSet.getString("idrol");
                    String nombre = resultSet.getString("nombre");
                    String contraseña = resultSet.getString("contraseña");

                    // Imprimir los resultados en la consola
                    System.out.println("ID: " + id + ", nombre: " + nombre + ", contraseña: " + contraseña);
                }
            } catch (SQLException e) {
                System.err.println("Error al ejecutar la consulta SQL: " + e.getMessage());
            }
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("hello-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 600, 600);
            stage.setTitle("LOGIN");
            stage.setScene(scene);
            stage.show();
        } else {
            // Manejar el caso de falla de conexión
            System.err.println("Error al conectar a la base de datos. La aplicación no puede continuar.");
            // Opcionalmente, puedes mostrar un mensaje de error al usuario
        }
    }

    public static void main(String[] args) {
        launch();
    }
}