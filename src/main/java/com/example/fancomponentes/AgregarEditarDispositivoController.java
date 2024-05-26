package com.example.fancomponentes;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AgregarEditarDispositivoController {

    @FXML
    private TableView<Componente> componentesTableView;

    @FXML
    private TableColumn<Componente, String> idComponenteColumn;

    @FXML
    private TableColumn<Componente, String> nombreColumn;

    @FXML
    private TableColumn<Componente, Integer> stockColumn;

    @FXML
    private TableColumn<Componente, Double> precioColumn;

    @FXML
    private TableColumn<Componente, String> descripcionColumn;

    @FXML
    private TextField nombreTextField;

    @FXML
    private TextArea descripcionTextArea;

    @FXML
    private TableView<Manual> manualTableView;

    @FXML
    private TableColumn<Manual, String> manualComponenteIdColumn;

    @FXML
    private TableColumn<Manual, String> manualComponenteNombreColumn;

    @FXML
    private TableColumn<Manual, Integer> manualCantidadColumn;

    private ObservableList<Manual> manualComponentes;

    @FXML
    private void initialize() {
        idComponenteColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nombreColumn.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        stockColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        precioColumn.setCellValueFactory(new PropertyValueFactory<>("precio"));
        descripcionColumn.setCellValueFactory(new PropertyValueFactory<>("descripcion"));

        manualComponenteIdColumn.setCellValueFactory(new PropertyValueFactory<>("componenteId"));
        manualComponenteNombreColumn.setCellValueFactory(new PropertyValueFactory<>("nombreComponente"));
        manualCantidadColumn.setCellValueFactory(new PropertyValueFactory<>("cantidad"));

        manualComponentes = FXCollections.observableArrayList();
        manualTableView.setItems(manualComponentes);

        cargarComponentes();
    }

    private void cargarComponentes() {
        try (Connection conexion = DatabaseConnector.getConexion()) {
            String consulta = "SELECT IDCOMPONENTE, NOMBRE, STOCK, PRECIO, DESCRIPCION FROM COMPONENTES";
            try (PreparedStatement declaracion = conexion.prepareStatement(consulta)) {
                ResultSet resultado = declaracion.executeQuery();
                ObservableList<Componente> componentes = FXCollections.observableArrayList();
                while (resultado.next()) {
                    String idComponente = resultado.getString("IDCOMPONENTE");
                    String nombre = resultado.getString("NOMBRE");
                    int stock = resultado.getInt("STOCK");
                    double precio = resultado.getDouble("PRECIO");
                    String descripcion = resultado.getString("DESCRIPCION");

                    Componente componente = new Componente(idComponente, nombre, stock, precio, descripcion);
                    componentes.add(componente);
                }
                componentesTableView.setItems(componentes);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void aniadirComponente() {
        Componente selectedComponente = componentesTableView.getSelectionModel().getSelectedItem();
        if (selectedComponente != null) {
            boolean encontrado = false;
            for (Manual manualComponente : manualComponentes) {
                if (manualComponente.getComponenteId().equals(selectedComponente.getId())) {
                    manualComponente.setCantidad(manualComponente.getCantidad() + 1);
                    encontrado = true;
                    break;
                }
            }
            if (!encontrado) {
                Manual manualComponente = new Manual(selectedComponente.getId(), selectedComponente.getNombre(), 1);
                manualComponentes.add(manualComponente);
            }
            manualTableView.refresh();
        }
    }

    @FXML
    private void guardarDispositivo(ActionEvent event) {
        String nombre = nombreTextField.getText();
        String descripcion = descripcionTextArea.getText();
        String cantidadStr = "1";
        String precioStr = "0";

        if (nombre.isEmpty() || descripcion.isEmpty()) {
            mostrarMensajeError("El nombre y la descripción no pueden estar vacíos.");
            return;
        }

        int cantidad = 1;
        double precio = 0.0;

        int dispositivoId = 0;

        try (Connection conexion = DatabaseConnector.getConexion()) {
            conexion.setAutoCommit(false);

            String insertarDispositivo = "INSERT INTO DISPOSITIVOS (NOMBRE, PRECIO, DESCRIPCION, CANTIDAD) VALUES (?, ?, ?, ?)";
            try (PreparedStatement declaracion = conexion.prepareStatement(insertarDispositivo, PreparedStatement.RETURN_GENERATED_KEYS)) {
                declaracion.setString(1, nombre);
                declaracion.setDouble(2, precio);
                declaracion.setString(3, descripcion);
                declaracion.setInt(4, cantidad);

                int affectedRows = declaracion.executeUpdate();
                if (affectedRows == 0) {
                    throw new SQLException("Error al insertar el dispositivo, no se insertaron filas.");
                }

                ResultSet generatedKeys = declaracion.getGeneratedKeys();
                if (generatedKeys.next()) {
                    dispositivoId = generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Error al obtener el ID del dispositivo insertado.");
                }
            }

            guardarManual(conexion, dispositivoId);
            conexion.commit();

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("GestionDispositivos.fxml"));
                Parent root = loader.load();
                GestionDispositivosController controller = loader.getController();

                Scene scene = new Scene(root);
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.show();

                Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                currentStage.close();
            } catch (IOException e) {
                e.printStackTrace();
                mostrarMensajeError("Error al cargar la ventana de gestión de dispositivos.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            mostrarMensajeError("Error al guardar el dispositivo: " + e.getMessage());
        }
    }

    private void guardarManual(Connection conexion, int dispositivoId) throws SQLException {
        String insertarManual = "INSERT INTO MANUAL (N_DISPOSITIVO, COMPONENTE, CANTIDAD) VALUES (?, ?, ?)";
        for (Manual componente : manualComponentes) {
            try (PreparedStatement declaracion = conexion.prepareStatement(insertarManual)) {
                declaracion.setInt(1, dispositivoId);
                declaracion.setString(2, componente.getComponenteId());
                declaracion.setInt(3, componente.getCantidad());
                declaracion.executeUpdate();
            }
        }
    }

    private void mostrarMensajeError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    @FXML
    private void cancelar(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}
