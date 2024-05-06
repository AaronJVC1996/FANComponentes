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
        // Initialize TableView columns
        idComponenteColumn.setCellValueFactory(new PropertyValueFactory<>("idComponente"));
        nombreColumn.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        precioColumn.setCellValueFactory(new PropertyValueFactory<>("precio"));
        stockColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));

        // Load data into TableView
        cargarComponentes();

        // Configure listener for TableView selection
        componentesTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                mostrarDescripcionComponentes(newValue.getNombre());
            }
        });
    }

    private void cargarComponentes() {
        try (Connection conexion = DatabaseConnector.getConexion()) {
            String consulta = "SELECT idcomponente, nombre, stock, precio FROM componentes";
            try (PreparedStatement declaracion = conexion.prepareStatement(consulta)) {
                ResultSet resultado = declaracion.executeQuery();
                while (resultado.next()) {
                    // Populate TableView
                    componentesTableView.getItems().add(new Componente(
                            resultado.getInt("idcomponente"),
                            resultado.getString("nombre"),
                            resultado.getInt("stock"),
                            resultado.getInt("precio")
                    ));

                    // Populate ListView
                    componentesListView.getItems().add(resultado.getString("nombre"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private void mostrarDescripcionComponentes(String nombreComponente) {
        try (Connection conexion = DatabaseConnector.getConexion()) {
            String consulta = "SELECT descripcion FROM componentes WHERE nombre = ?";
            try (PreparedStatement declaracion = conexion.prepareStatement(consulta)) {
                declaracion.setString(1, nombreComponente);
                ResultSet resultado = declaracion.executeQuery();
                if (resultado.next()) {
                    descripcionLabel.setText(resultado.getString("descripcion"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


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