module com.example.myfirstproject {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires mysql.connector.j;

    opens com.example.myfirstgame to javafx.fxml;
    exports com.example.myfirstgame;
    exports controller;
	exports model;
	exports view;
    opens controller to javafx.fxml;
}