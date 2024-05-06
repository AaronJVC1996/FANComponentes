package com.example.fancomponentes;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;

import java.sql.*;

public class GestionAlmacenController {
    @FXML
    private ListView<String> componentesListView;

    @FXML
    private TableColumn<String, Integer> idComponenteColumn;

    @FXML
    private TableColumn<String, String> nombreColumn;

    @FXML
    private TableColumn<String, Integer> stockColumn;

    @FXML
    private TableColumn<String, Double> precioColumn;


    @FXML
    private Label descripcionLabel;

    @FXML
    private void initialize() {
        // Cargar los nombres de los componentes desde la base de datos
        cargarComponentes();

        // Configurar el listener para el evento de selección en el ListView (obtenido de internet)
        componentesListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            // Mostrar los detalles del empleado seleccionado
            mostrarDetallesComponentes(newValue);
        });
    }

    // Metodo para cargar los nombres de los componentes desde la base de datos y mostrarlos en el ListView
    private void cargarComponentes() {
        try (Connection conexion = DriverManager.getConnection("jdbc:mysql://10.168.58.2:3306/fancomponentes", "root", "Dam1bSql01")) {
            String consulta = "SELECT idcomponente, nombre, stock, precio, descripcion FROM componentes";
            try (PreparedStatement declaracion = conexion.prepareStatement(consulta)) {
                ResultSet resultado = declaracion.executeQuery();
                while (resultado.next()) {
                    String id = resultado.getString("idcomponente");
                    String nombre = resultado.getString("nombre");
                    int stock = resultado.getInt("stock");
                    double precio = resultado.getDouble("precio");
                    String descripcion = resultado.getString("descripcion");

                    // Create a new Componente object
                    Componente componente = new Componente(id, nombre, stock, precio, descripcion);

                    // Add the Componente object to the ListView
                    componentesListView.getItems().add(componente.getNombre()); // Displaying the name in the ListView
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
                    idComponenteColumn.setText(resultado.getString("idcomponente"));
                    nombreColumn.setText(resultado.getString("nombre"));
                    stockColumn.setText(resultado.getString("stock"));
                    precioColumn.setText(resultado.getString("precio"));
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