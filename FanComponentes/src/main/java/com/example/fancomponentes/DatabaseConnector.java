package com.example.fancomponentes;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnector {
    // URL de conexión de MySQL
    private static final String URL = "jdbc:mysql://10.168.58.2:3306/fancomponentes";
    private static final String USUARIO = "root";
    private static final String CONTRASEÑA = "Dam1bSql01";

    // Método para establecer la conexión con la base de datos
    public static Connection conectar() {
        Connection conn = null;
        try {
            // Registrar el driver JDBC de MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establecer la conexión con la base de datos
            conn = DriverManager.getConnection(URL, USUARIO, CONTRASEÑA);
            System.out.println("Conexión exitosa a la base de datos.");
            System.out.println("-----------------------------------");
            System.out.println("-----------------------------------");
            System.out.println("-----------------------------------");
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Error al conectar a la base de datos: " + e.getMessage());
        }
        return conn;
    }
}