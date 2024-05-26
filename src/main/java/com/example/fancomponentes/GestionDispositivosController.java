package com.example.fancomponentes;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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
    private Button btnSumar;

    @FXML
    private Button btnRestar;
// Inicializa los dispositivos, sus descripciones y manuales.
    @FXML
    private void initialize() {
        // Configuración inicial de las columnas
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
// Carga los dispositivos existentes en el TableView
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
// Muestra la descripcion de el dispositivo seleccionado
    @FXML
    private void mostrarDescripcion() {
        Dispositivo selectedDispositivo = dispositivosTableView.getSelectionModel().getSelectedItem();
        if (selectedDispositivo != null) {
            detalleDescripcionTextArea.setWrapText(true); // Ajusta el valor según necesites
            detalleDescripcionTextArea.setText(selectedDispositivo.getDescripcion());
        }
    }
// Carga el manual del dispositivo seleccionado y el precio de los componentes y su cantidades que compongan
// dicho manual, sera el precio por unidad del dispositivo
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
                    dispositivo.setPrecio(precioTotal * dispositivo.getCantidad());
                    dispositivosTableView.refresh();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            manualTableView.getItems().clear();
        }
    }

    //Este metodo aumenta uno a cantidad de dispositivo y con ello aumenta el precio y reduce la cantidad de componentes.
// verifica si hay suficientes componentes para crear un dispositivo, si hay suficientes componentes
// el dispositivo sera creado, pero si no hay suficientes componentes saltara un error sobre esto.
    @FXML
    private void incrementarCantidad() {
        Dispositivo selectedDispositivo = dispositivosTableView.getSelectionModel().getSelectedItem();
        if (selectedDispositivo != null) {
            if (verificarExistenciasComponentes(selectedDispositivo, 1)) {
                actualizarStockComponentes(selectedDispositivo, -1);  // Reducir stock de componentes
                selectedDispositivo.setCantidad(selectedDispositivo.getCantidad() + 1);
                actualizarCantidadDispositivo(selectedDispositivo);
                actualizarPrecioDispositivo(selectedDispositivo); // Actualizar el precio del dispositivo
                dispositivosTableView.refresh();
            } else { // Usamos el metodo mostrarMensajeError y le damos un valor al parametro que pide(el mensaje).
                mostrarMensajeError("No hay suficientes existencias de componentes para añadir más dispositivos.");
            }
        }
    }
// Resta uno a dispositivo y con ello aumenta sus componentes y disminuye su precio
    @FXML
    private void decrementarCantidad() {
        Dispositivo selectedDispositivo = dispositivosTableView.getSelectionModel().getSelectedItem();
        if (selectedDispositivo != null && selectedDispositivo.getCantidad() > 0) {
            actualizarStockComponentes(selectedDispositivo, 1); // Aumentar stock de componentes
            selectedDispositivo.setCantidad(selectedDispositivo.getCantidad() - 1);
            actualizarCantidadDispositivo(selectedDispositivo);
            actualizarPrecioDispositivo(selectedDispositivo); // Actualizar el precio del dispositivo
            dispositivosTableView.refresh();
        }
    }
// Este metodo actualiza el precio del dispositivo segun los componentes de su manual y la cantidad de dispositivos
// este metodo es usado en incrementar o decrementar cantidad
    private void actualizarPrecioDispositivo(Dispositivo dispositivo) {
        try (Connection conexion = DatabaseConnector.getConexion()) {
            String consulta = "SELECT M.COMPONENTE, C.PRECIO, M.CANTIDAD FROM MANUAL M JOIN COMPONENTES C ON M.COMPONENTE = C.IDCOMPONENTE WHERE M.N_DISPOSITIVO = ?";
            try (PreparedStatement declaracion = conexion.prepareStatement(consulta)) {
                declaracion.setInt(1, dispositivo.getNDispositivo());
                ResultSet resultado = declaracion.executeQuery();
                double precioTotal = 0.0;
                while (resultado.next()) {
                    double precioComponente = resultado.getDouble("PRECIO");
                    int cantidadComponente = resultado.getInt("CANTIDAD");
                    precioTotal += precioComponente * cantidadComponente;
                }
                dispositivo.setPrecio(precioTotal * dispositivo.getCantidad());
                dispositivosTableView.refresh();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
// Este metodo realizara la actualizacion de la cantidad de componentes segun la cantidad de dispositivos
// utilizara el metodo de abajo para hacer un update con los datos de este metodo, que tambien itera sobre cada componente
// este metodo es usado en incrementar o decrementar cantidad
    private void actualizarStockComponentes(Dispositivo dispositivo, int factor) {
        if (dispositivo != null) {
            try (Connection conexion = DatabaseConnector.getConexion()) {
                String consulta = "SELECT COMPONENTE, CANTIDAD FROM MANUAL WHERE N_DISPOSITIVO = ?";
                try (PreparedStatement declaracion = conexion.prepareStatement(consulta)) {
                    declaracion.setInt(1, dispositivo.getNDispositivo());
                    ResultSet resultado = declaracion.executeQuery();
                    while (resultado.next()) {
                        String componenteId = resultado.getString("COMPONENTE");
                        int cantidadComponente = resultado.getInt("CANTIDAD");

                        actualizarStockComponente(conexion, componenteId, cantidadComponente * factor);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
// Este metodo sera el base para actualizar el stock y usarlo en el metodo de arriba.
    private void actualizarStockComponente(Connection conexion, String componenteId, int cantidad) {
        String actualizarStock = "UPDATE COMPONENTES SET STOCK = STOCK + ? WHERE IDCOMPONENTE = ?";
        try (PreparedStatement declaracion = conexion.prepareStatement(actualizarStock)) {
            declaracion.setInt(1, cantidad);
            declaracion.setString(2, componenteId);
            declaracion.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
//Este metodo ahora toma en cuenta la cantidad actual de dispositivos
//y el incremento deseado para asegurar que hay suficientes existencias de componentes antes de permitir el incremento
    private boolean verificarExistenciasComponentes(Dispositivo dispositivo, int incremento) {
        if (dispositivo != null) {
            try (Connection conexion = DatabaseConnector.getConexion()) {
                String consulta = "SELECT COMPONENTE, CANTIDAD FROM MANUAL WHERE N_DISPOSITIVO = ?";
                try (PreparedStatement declaracion = conexion.prepareStatement(consulta)) {
                    declaracion.setInt(1, dispositivo.getNDispositivo());
                    ResultSet resultado = declaracion.executeQuery();
                    while (resultado.next()) {
                        String componenteId = resultado.getString("COMPONENTE");
                        int cantidadNecesaria = resultado.getInt("CANTIDAD") * incremento;

                        // Verificar el stock actual del componente
                        if (!haySuficienteStock(componenteId, cantidadNecesaria)) {
                            return false;
                        }
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return true;
    }
//Este metodo verifica si hay suficiente stock de componentes considerando la cantidad total
    private boolean haySuficienteStock(String componenteId, int cantidadNecesaria) {
        try (Connection conexion = DatabaseConnector.getConexion()) {
            String consulta = "SELECT STOCK FROM COMPONENTES WHERE IDCOMPONENTE = ?";
            try (PreparedStatement declaracion = conexion.prepareStatement(consulta)) {
                declaracion.setString(1, componenteId);
                ResultSet resultado = declaracion.executeQuery();
                if (resultado.next()) {
                    int stockActual = resultado.getInt("STOCK");
                    return stockActual >= cantidadNecesaria;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
// Este metodo actualiza la cantidad de dispositivos en el dispositivo indicado
// este metodo es usado en incrementar o decrementar cantidad
    private void actualizarCantidadDispositivo(Dispositivo dispositivo) {
        try (Connection conexion = DatabaseConnector.getConexion()) {
            String actualizarCantidad = "UPDATE DISPOSITIVOS SET CANTIDAD = ? WHERE N_DISPOSITIVO = ?";
            try (PreparedStatement declaracion = conexion.prepareStatement(actualizarCantidad)) {
                declaracion.setInt(1, dispositivo.getCantidad());
                declaracion.setInt(2, dispositivo.getNDispositivo());
                declaracion.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
// Ventana del mensaje de error en caso de que no se puedan crear mas dispositivos debido a que no hay
// suficientes existencias de componentes para crearlo
    private void mostrarMensajeError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
    @FXML
    private void abrirVentanaAgregarEditarDispositivo(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AgregarDispositivo.fxml"));
            Parent root = loader.load();

            // Obtener el controlador de la ventana AgregarEditarDispositivo
            AgregarDispositivoController controller = loader.getController();

            // Configurar cualquier dato necesario en el controlador de la ventana AgregarEditarDispositivo

            // Crear un nuevo Stage para la ventana AgregarEditarDispositivo
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Agregar o Editar Dispositivo");

            // Mostrar la ventana
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}