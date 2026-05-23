module com.example.oopworkshopv2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    requires com.dlsc.formsfx;
    requires org.postgresql.jdbc;
    requires io.github.cdimascio.dotenv.java;

    opens com.example.oopworkshopv2 to javafx.fxml;
    opens com.example.oopworkshopv2.controller to javafx.fxml, javafx.base;
    exports com.example.oopworkshopv2;
}
