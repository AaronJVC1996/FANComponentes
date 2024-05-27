package com.example.fancomponentes;

import com.example.fancomponentes.Componente;
import com.example.fancomponentes.DatabaseConnector;
import com.example.fancomponentes.GestionDispositivosController;
import com.example.fancomponentes.Manual;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.sql.*;

public class AgregarDispositivoController {

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
    private GestionDispositivosController gestionDispositivosController;
    private Stage stage;

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

    public void setGestionDispositivosController(GestionDispositivosController gestionDispositivosController) {
        this.gestionDispositivosController = gestionDispositivosController;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
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
    private void guardarDispositivo(ActionEvent event) {
        String nombre = nombreTextField.getText();
        String descripcion = descripcionTextArea.getText();

        if (nombre.isEmpty() || descripcion.isEmpty()) {
            mostrarMensajeError("El nombre y la descripción no pueden estar vacíos.");
            return;
        }

        try (Connection conexion = DatabaseConnector.getConexion()) {
            String insertarDispositivo = "INSERT INTO DISPOSITIVOS (NOMBRE, DESCRIPCION) VALUES (?, ?)";
            try (PreparedStatement declaracion = conexion.prepareStatement(insertarDispositivo, Statement.RETURN_GENERATED_KEYS)) {
                declaracion.setString(1, nombre);
                declaracion.setString(2, descripcion);
                declaracion.executeUpdate();

                ResultSet generatedKeys = declaracion.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int dispositivoId = generatedKeys.getInt(1);
                    guardarManual(conexion, dispositivoId);
                }
            }

            mostrarMensajeConfirmacion("Dispositivo guardado con éxito.");
            // Llama a setGestionDispositivosController antes de cerrar la ventana
            setGestionDispositivosController(gestionDispositivosController);
            gestionDispositivosController.cargarDispositivos();
            cerrarVentana(event);
        } catch (SQLException e) {
            e.printStackTrace();
            mostrarMensajeError("Error al guardar el dispositivo: " + e.getMessage());
        }
    }

    private void mostrarMensajeConfirmacion(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmación");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void cerrarVentana(ActionEvent event) {
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.close();
        if (stage != null) {
            stage.show();
        }
    }

    private void guardarManual(Connection conexion, int dispositivoId) throws SQLException {
        String insertarManual = "INSERT INTO MANUAL (N_DISPOSITIVO, COMPONENTE, CANTIDAD) VALUES (?, ?, ?)";
        double precioTotal = 0.0;

        for (Manual componente : manualComponentes) {
            // Insertar componente en la tabla MANUAL
            try (PreparedStatement declaracion = conexion.prepareStatement(insertarManual)) {
                declaracion.setInt(1, dispositivoId);
                declaracion.setString(2, componente.getComponenteId());
                declaracion.setInt(3, componente.getCantidad());
                declaracion.executeUpdate();
            }

            // Calcular el precio total del dispositivo basado en los componentes
            String consultaPrecio = "SELECT PRECIO FROM COMPONENTES WHERE IDCOMPONENTE = ?";
            try (PreparedStatement declaracion = conexion.prepareStatement(consultaPrecio)) {
                declaracion.setString(1, componente.getComponenteId());
                ResultSet resultado = declaracion.executeQuery();
                if (resultado.next()) {
                    double precioComponente = resultado.getDouble("PRECIO");
                    precioTotal += precioComponente * componente.getCantidad();
                }
            }
        }

        // Actualizar el precio total del dispositivo en la tabla DISPOSITIVOS
        String actualizarPrecioDispositivo = "UPDATE DISPOSITIVOS SET PRECIO = ? WHERE N_DISPOSITIVO = ?";
        try (PreparedStatement declaracion = conexion.prepareStatement(actualizarPrecioDispositivo)) {
            declaracion.setDouble(1, precioTotal);
            declaracion.setInt(2, dispositivoId);
            declaracion.executeUpdate();
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
