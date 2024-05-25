package com.example.fancomponentes;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GestionDispositivosController {

    @FXML
    private TableView<Dispositivo> dispositivosTableView;

    @FXML
    private TableColumn<Dispositivo, Integer> idColumn;

    @FXML
    private TableColumn<Dispositivo, String> nombreColumn;

    @FXML
    private TableColumn<Dispositivo, Double> precioColumn;

    @FXML
    private TableColumn<Dispositivo, Integer> stockColumn;

    @FXML
    private TextArea detalleDescripcionTextArea;

    @FXML
    private TableView<Manual> manualTableView;

    @FXML
    private TableColumn<Manual, String> manualComponenteIdColumn;

    @FXML
    private TableColumn<Manual, String> manualComponenteNombreColumn;

    @FXML
    private TableColumn<Manual, Integer> manualCantidadColumn;

    @FXML
    private void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("nDispositivo"));
        nombreColumn.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        precioColumn.setCellValueFactory(new PropertyValueFactory<>("precio"));
        stockColumn.setCellValueFactory(new PropertyValueFactory<>("cantidad"));

        manualComponenteIdColumn.setCellValueFactory(new PropertyValueFactory<>("componenteId"));
        manualComponenteNombreColumn.setCellValueFactory(new PropertyValueFactory<>("nombreComponente"));
        manualCantidadColumn.setCellValueFactory(new PropertyValueFactory<>("cantidad"));

        dispositivosTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            mostrarDescripcion();
            cargarManual(newValue);
        });

        cargarDispositivos();
    }

    private void cargarDispositivos() {
        try (Connection conexion = DatabaseConnector.getConexion()) {
            String consulta = "SELECT N_DISPOSITIVO, NOMBRE, PRECIO, DESCRIPCION, CANTIDAD FROM DISPOSITIVOS";
            try (PreparedStatement declaracion = conexion.prepareStatement(consulta)) {
                ResultSet resultado = declaracion.executeQuery();
                ObservableList<Dispositivo> dispositivos = FXCollections.observableArrayList();
                while (resultado.next()) {
                    int nDispositivo = resultado.getInt("N_DISPOSITIVO");
                    String nombre = resultado.getString("NOMBRE");
                    double precio = resultado.getDouble("PRECIO");
                    String descripcion = resultado.getString("DESCRIPCION");
                    int cantidad = resultado.getInt("CANTIDAD");

                    Dispositivo dispositivo = new Dispositivo(nDispositivo, nombre, precio, descripcion, cantidad);
                    dispositivos.add(dispositivo);
                }
                dispositivosTableView.setItems(dispositivos);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void refrescarLista() {
        dispositivosTableView.getItems().clear();
        cargarDispositivos();
    }

    @FXML
    private void mostrarDescripcion() {
        Dispositivo selectedDispositivo = dispositivosTableView.getSelectionModel().getSelectedItem();
        if (selectedDispositivo != null) {
            detalleDescripcionTextArea.setWrapText(true); // Ajusta el valor según necesites
            detalleDescripcionTextArea.setText(selectedDispositivo.getDescripcion());
        }
    }

    private void cargarManual(Dispositivo dispositivo) {
        if (dispositivo != null) {
            try (Connection conexion = DatabaseConnector.getConexion()) {
                String consulta = "SELECT M.COMPONENTE, C.NOMBRE, M.CANTIDAD, C.PRECIO FROM MANUAL M JOIN COMPONENTES C ON M.COMPONENTE = C.IDCOMPONENTE WHERE M.N_DISPOSITIVO = ?";
                try (PreparedStatement declaracion = conexion.prepareStatement(consulta)) {
                    declaracion.setInt(1, dispositivo.getNDispositivo());
                    ResultSet resultado = declaracion.executeQuery();
                    ObservableList<Manual> manuales = FXCollections.observableArrayList();
                    double precioTotal = 0.0;
                    while (resultado.next()) {
                        String componenteId = resultado.getString("COMPONENTE");
                        String nombreComponente = resultado.getString("NOMBRE");
                        int cantidad = resultado.getInt("CANTIDAD");
                        double precioComponente = resultado.getDouble("PRECIO");

                        // Calcular el precio total del dispositivo
                        precioTotal += precioComponente * cantidad;

                        Manual manual = new Manual(componenteId, nombreComponente, cantidad);
                        manuales.add(manual);
                    }
                    manualTableView.setItems(manuales);

                    // Actualizar el precio del dispositivo seleccionado
                    dispositivo.setPrecio(precioTotal);
                    dispositivosTableView.refresh();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            manualTableView.getItems().clear();
        }
    }
}