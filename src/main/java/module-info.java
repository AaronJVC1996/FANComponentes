module com.example.fancomponentes {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.j;
    requires java.desktop;


    opens com.example.fancomponentes to javafx.fxml;
    exports com.example.fancomponentes;
}