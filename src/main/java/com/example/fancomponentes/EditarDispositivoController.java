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
import java.sql.*;

public class EditarDispositivoController {

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
    private Dispositivo dispositivo;

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
        aplicarEstiloFilas();
    }

    public void setDispositivo(Dispositivo dispositivo) {
        this.dispositivo = dispositivo;
        cargarDatosDispositivo();
    }

    private void cargarDatosDispositivo() {
        if (dispositivo != null) {
            nombreTextField.setText(dispositivo.getNombre());
            descripcionTextArea.setText(dispositivo.getDescripcion());
            cargarManualDispositivo();
        }
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

    private void cargarManualDispositivo() {
        try (Connection conexion = DatabaseConnector.getConexion()) {
            String consulta = "SELECT COMPONENTE, CANTIDAD FROM MANUAL WHERE N_DISPOSITIVO = ?";
            try (PreparedStatement declaracion = conexion.prepareStatement(consulta)) {
                declaracion.setInt(1, dispositivo.getNDispositivo());
                ResultSet resultado = declaracion.executeQuery();
                while (resultado.next()) {
                    String componenteId = resultado.getString("COMPONENTE");
                    int cantidad = resultado.getInt("CANTIDAD");
                    String nombreComponente = obtenerNombreComponente(conexion, componenteId);

                    Manual manualComponente = new Manual(componenteId, nombreComponente, cantidad);
                    manualComponentes.add(manualComponente);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private String obtenerNombreComponente(Connection conexion, String componenteId) throws SQLException {
        String consulta = "SELECT NOMBRE FROM COMPONENTES WHERE IDCOMPONENTE = ?";
        try (PreparedStatement declaracion = conexion.prepareStatement(consulta)) {
            declaracion.setString(1, componenteId);
            ResultSet resultado = declaracion.executeQuery();
            if (resultado.next()) {
                return resultado.getString("NOMBRE");
            } else {
                return null;
            }
        }
    }

    @FXML
    private void aniadirComponente() {
        Componente selectedComponente = componentesTableView.getSelectionModel().getSelectedItem();
        if (selectedComponente != null) {
            boolean encontrado = false;
            for (Manual manualComponente : manualComponentes) {
                if (manualComponente.getComponenteId().equals(selectedComponente.getId())) {
                    if (manualComponente.getCantidad() < selectedComponente.getStock()) {
                        manualComponente.setCantidad(manualComponente.getCantidad() + 1);
                    } else {
                        mostrarMensajeAdvertencia("No puedes añadir más de este componente, no hay suficiente stock.");
                    }
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
    private void quitarComponente() {
        Manual selectedManualComponente = manualTableView.getSelectionModel().getSelectedItem();
        if (selectedManualComponente != null) {
            if (selectedManualComponente.getCantidad() > 1) {
                selectedManualComponente.setCantidad(selectedManualComponente.getCantidad() - 1);
            } else {
                manualComponentes.remove(selectedManualComponente);
            }
            manualTableView.refresh();
        }
    }

    @FXML
    private void actualizarDispositivo(ActionEvent event) {
        String nombre = nombreTextField.getText();
        String descripcion = descripcionTextArea.getText();

        if (nombre.isEmpty() || descripcion.isEmpty()) {
            mostrarMensajeError("El nombre y la descripción no pueden estar vacíos.");
            return;
        }

        try (Connection conexion = DatabaseConnector.getConexion()) {
            conexion.setAutoCommit(false);

            String actualizarDispositivo = "UPDATE DISPOSITIVOS SET NOMBRE = ?, DESCRIPCION = ? WHERE N_DISPOSITIVO = ?";
            try (PreparedStatement declaracion = conexion.prepareStatement(actualizarDispositivo)) {
                declaracion.setString(1, nombre);
                declaracion.setString(2, descripcion);
                declaracion.setInt(3, dispositivo.getNDispositivo());
                declaracion.executeUpdate();
            }

            eliminarManual(conexion, dispositivo.getNDispositivo());
            guardarManual(conexion, dispositivo.getNDispositivo());
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
            mostrarMensajeError("Error al actualizar el dispositivo: " + e.getMessage());
        }
    }

    private void eliminarManual(Connection conexion, int dispositivoId) throws SQLException {
        String eliminarManual = "DELETE FROM MANUAL WHERE N_DISPOSITIVO = ?";
        try (PreparedStatement declaracion = conexion.prepareStatement(eliminarManual)) {
            declaracion.setInt(1, dispositivoId);
            declaracion.executeUpdate();
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

    private void mostrarMensajeAdvertencia(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Advertencia");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    @FXML
    private void cancelar(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    private void aplicarEstiloFilas() {
        componentesTableView.setRowFactory(tv -> new TableRow<Componente>() {
            @Override
            protected void updateItem(Componente item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null) {
                    setStyle("");
                } else {
                    Integer stock = item.getStock();
                    if (stock == null) {
                        setStyle("");
                    } else {
                        if (stock <= 5 && stock > 0) {
                            getStyleClass().removeAll("low-stock", "out-of-stock");
                            getStyleClass().add("low-stock");
                        } else if (stock == 0) {
                            getStyleClass().removeAll("low-stock", "out-of-stock");
                            getStyleClass().add("out-of-stock");
                        } else {
                            getStyleClass().removeAll("low-stock", "out-of-stock");
                        }
                    }
                }
            }
        });
    }
}