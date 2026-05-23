module com.example.oopworkshopv2 {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;

    opens com.example.oopworkshopv2 to javafx.fxml;
    exports com.example.oopworkshopv2;
}