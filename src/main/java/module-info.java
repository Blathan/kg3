module com.example.kg3 {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.example.kg3 to javafx.fxml;

    exports com.example.kg3;
}