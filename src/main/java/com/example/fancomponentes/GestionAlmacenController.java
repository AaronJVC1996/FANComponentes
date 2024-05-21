package com.example.fancomponentes;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.scene.text.Text;
import javafx.scene.input.MouseEvent;


import java.sql.*;

public class GestionAlmacenController {
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
    private Text descripcionText;

    @FXML
    private void initialize() {



        // Cargar los nombres de los componentes desde la base de datos


        // Configurar el listener para el evento de selección en el ListView (obtenido de internet)
        componentesTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            // Mostrar los detalles del empleado seleccionado
            idComponenteColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

            nombreColumn.setCellValueFactory(new PropertyValueFactory<>("nombre"));
            precioColumn.setCellValueFactory(new PropertyValueFactory<>("precio"));
            stockColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));

            componentesTableView.setOnMouseClicked(this::mostrarDescripcion);


        });

        componentesTableView.refresh();
        cargarComponentes();

        aplicarEstiloFilas();
    }

    // Metodo para cargar los nombres de los componentes desde la base de datos y mostrarlos en el ListView
    private void cargarComponentes() {
        try (Connection conexion = DatabaseConnector.getConexion()) {
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
                    this.componentesTableView.getItems().add(componente); // Displaying the name in the ListView
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private void mostrarDetallesComponentes(String nombreComponente) {
        // Consulta ala base de datos para saber los detalles de los componentes
        try (Connection conexion = DatabaseConnector.getConexion()) {
            String consulta = "SELECT idcomponente, nombre, stock, precio, descripcion FROM componentes WHERE nombre = ?";
            try (PreparedStatement declaracion = conexion.prepareStatement(consulta)) {
                declaracion.setString(1, nombreComponente);
                ResultSet resultado = declaracion.executeQuery();
                if (resultado.next()) {
                    idComponenteColumn.setText(resultado.getString("idcomponente"));
                    nombreColumn.setText(resultado.getString("nombre"));
                    stockColumn.setText(resultado.getString("stock"));
                    precioColumn.setText(resultado.getString("precio"));
                    descripcionText.setText(resultado.getString("descripcion"));
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
        componentesTableView.getItems().clear();
        cargarComponentes();
    }
    @FXML
        private void aniadirStock() {
            Componente componenteSeleccionado = componentesTableView.getSelectionModel().getSelectedItem();
            if (componenteSeleccionado != null) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("AniadirStock.fxml"));
                    Parent root = loader.load();

                    AniadirStockController controller = loader.getController();
                    controller.setComponenteId(componenteSeleccionado.getId());

                    Stage stage = new Stage();
                    stage.setScene(new Scene(root));
                    stage.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Ningún Componente Seleccionado");
                alert.setContentText("Por favor, selecciona un componente para añadir stock.");
                alert.showAndWait();
            }
        }
    @FXML
    private void aniadirComponente(ActionEvent event) {

                try {
                    // Cargar la vista para agregar nuevo componente desde su archivo FXML
                    Parent agregar = FXMLLoader.load(getClass().getResource("NuevosComponentes.fxml"));
                    Stage stage = new Stage();
                    stage.setScene(new Scene(agregar));
                    stage.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
    @FXML
    private void mostrarDescripcion(MouseEvent event) {
        Componente selectedComponente = componentesTableView.getSelectionModel().getSelectedItem();
        if (selectedComponente != null) {
            descripcionText.setWrappingWidth(200);  // Ajusta el valor según necesites
            descripcionText.setText(selectedComponente.getDescripcion());
        }
    }

    private void aplicarEstiloFilas() {
        componentesTableView.setRowFactory(tv -> new TableRow<Componente>() {
            @Override
            protected void updateItem(Componente item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setStyle("");
                    getStyleClass().removeAll("low-stock", "out-of-stock");
                } else {
                    Integer stock = item.getStock(); // Usamos Integer en lugar de int
                    getStyleClass().removeAll("low-stock", "out-of-stock");
                    if (stock == null) {
                        setStyle("");
                    } else if (stock <= 5 && stock > 0) {
                        getStyleClass().add("low-stock");
                    } else if (stock == 0) {
                        getStyleClass().add("out-of-stock");
                    }
                }
            }
        });
    }

}
