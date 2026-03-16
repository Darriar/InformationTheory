module com.space.laba_2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires kotlin.stdlib;
    requires java.desktop;


    opens com.example to javafx.fxml;
    exports com.example;
}