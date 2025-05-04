module gtp.atp {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires java.desktop;
    requires java.logging;

    opens gtp.atp to javafx.fxml;
    opens gtp.atp.controller to javafx.fxml;
    exports gtp.atp;
    exports gtp.atp.ui;
    exports gtp.atp.controller;
    exports gtp.atp.model;
    exports gtp.atp.service;
    exports gtp.atp.util;
    exports gtp.atp.exception;
}