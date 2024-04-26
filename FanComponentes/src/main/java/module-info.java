module com.example.fancomponentes {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.fancomponentes to javafx.fxml;
    exports com.example.fancomponentes;
}