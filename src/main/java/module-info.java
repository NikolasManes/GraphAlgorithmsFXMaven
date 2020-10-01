module com.nikolas {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.nikolas to javafx.fxml;
    exports com.nikolas;
}