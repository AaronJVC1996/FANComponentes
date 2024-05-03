package com.example.fancomponentes;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.sql.*;

public class GestionAlmacenController {
    @FXML
    private ListView<String> componentesListView;

    @FXML
    private Label idComponenteLabel;

    @FXML
    private Label nombreLabel;

    @FXML
    private Label stockLabel;

    @FXML
    private Label precioLabel;

    @FXML
    private Label descripcionLabel;

    @FXML
    private void initialize() {
        // Cargar los nombres de los empleados desde la base de datos
        cargarComponentes();

        // Configurar el listener para el evento de selección en el ListView (obtenido de internet)
        componentesListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            // Mostrar los detalles del empleado seleccionado
            mostrarDetallesComponentes(newValue);
        });
    }

    // Metodo para cargar los nombres de los empleados desde la base de datos y mostrarlos en el ListView
    private void cargarComponentes() {
        try (Connection conexion = DriverManager.getConnection("jdbc:mysql://10.168.58.2:3306/fancomponentes", "root", "Dam1bSql01")) {
            String consulta = "SELECT nombre FROM componentes";
            try (PreparedStatement declaracion = conexion.prepareStatement(consulta)) {
                ResultSet resultado = declaracion.executeQuery();
                while (resultado.next()) {
                    componentesListView.getItems().add(resultado.getString("nombre"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void mostrarDetallesComponentes(String nombreComponente) {
        // Consulta ala base de datos para saber los detalles de los componentes
        try (Connection conexion = DriverManager.getConnection("jdbc:mysql://10.168.58.2:3306/fancomponentes", "root", "Dam1bSql01")) {
            String consulta = "SELECT idcomponente, nombre, stock, precio, descripcion FROM componentes WHERE nombre = ?";
            try (PreparedStatement declaracion = conexion.prepareStatement(consulta)) {
                declaracion.setString(1, nombreComponente);
                ResultSet resultado = declaracion.executeQuery();
                if (resultado.next()) {
                    idComponenteLabel.setText(resultado.getString("idcomponente"));
                    nombreLabel.setText(resultado.getString("nombre"));
                    stockLabel.setText(resultado.getString("stock"));
                    precioLabel.setText(resultado.getString("precio"));
                    descripcionLabel.setText(resultado.getString("descripcion"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    // Metodo para eliminar un empleado seleccionado

    // Actualizar componentes
    @FXML
    private void refrescarLista() {
        // limpia y vuelve a cargar los componentes
        componentesListView.getItems().clear();
        cargarComponentes();
    }
    @FXML
    private void aniadirStock() {

        //pendiente


    }
    @FXML
    private void aniadirComponente() {

        //pendiente


    }




}