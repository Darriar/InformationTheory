module com.example.laba_1 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.laba_1 to javafx.fxml;
    exports com.example.laba_1;
}