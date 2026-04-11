module com.darya.rabin {
    requires javafx.controls;
    requires javafx.fxml;
    requires kotlin.stdlib;


    opens com.darya.rabin to javafx.fxml;
    exports com.darya.rabin;
}