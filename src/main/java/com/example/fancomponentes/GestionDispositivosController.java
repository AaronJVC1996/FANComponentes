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
    private void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("nDispositivo"));
        nombreColumn.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        precioColumn.setCellValueFactory(new PropertyValueFactory<>("precio"));
        stockColumn.setCellValueFactory(new PropertyValueFactory<>("cantidad"));

        dispositivosTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> mostrarDescripcion());

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
}

    /*
    @FXML
    private void aniadirStock() {
        Dispositivo dispositivoSeleccionado = dispositivosTableView.getSelectionModel().getSelectedItem();
        if (dispositivoSeleccionado != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("AniadirStock.fxml"));
                Parent root = loader.load();

                AniadirStockController controller = loader.getController();
                controller.setDispositivoId(dispositivoSeleccionado.getNDispositivo());

                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Ningún Dispositivo Seleccionado");
            alert.setContentText("Por favor, selecciona un dispositivo para añadir stock.");
            alert.showAndWait();
        }
    }

    @FXML
    private void aniadirDispositivo(ActionEvent event) {
        try {
            Parent agregar = FXMLLoader.load(getClass().getResource("NuevosDispositivos.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(agregar));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
     */