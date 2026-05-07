module com.example.sudokump1 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.sudokump1 to javafx.fxml;
    exports com.example.sudokump1;
}