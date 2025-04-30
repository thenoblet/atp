module gtp.atp {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;

    opens gtp.atp to javafx.fxml;
    exports gtp.atp;
}