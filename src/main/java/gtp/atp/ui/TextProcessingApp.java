package gtp.atp.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.Arrays;
import java.util.logging.*;

import static gtp.atp.service.LoggerService.LOGGER;
import static gtp.atp.service.LoggerService.configureLogging;


public class TextProcessingApp extends Application {

    static {
        configureLogging();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        LOGGER.log(Level.INFO, "Starting TextProcessingApp...");
        long startTime = System.currentTimeMillis();

        try {
            LOGGER.fine(() -> "Loading FXML file for welcome view");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/atp/view/welcomeview.fxml"));
            Parent root = loader.load();
            LOGGER.fine("FXML file loaded successfully");

            Scene scene = new Scene(root, 800, 600);
            LOGGER.config(() -> String.format("Created main scene with dimensions %dx%d",
                    (int) scene.getWidth(), (int) scene.getHeight()));

            primaryStage.initStyle(StageStyle.UNDECORATED);
            primaryStage.setScene(scene);
            primaryStage.setTitle("DataFlow Solutions - Text Processing Tool");

            primaryStage.setOnShown(e -> LOGGER.fine("Primary stage shown"));
            primaryStage.setOnCloseRequest(e -> LOGGER.info("Primary stage close requested"));

            LOGGER.fine("Preparing to show primary stage");
            primaryStage.show();

            long loadTime = System.currentTimeMillis() - startTime;
            LOGGER.info(() -> String.format("Application started successfully in %d ms", loadTime));
        } catch (Exception e) {
            long failTime = System.currentTimeMillis() - startTime;
            LOGGER.log(Level.SEVERE,
                    String.format("Application failed to start after %d ms", failTime), e);
            showErrorAlert(e);
            System.exit(1);
        }
    }

    private void showErrorAlert(Throwable e) {
        try {
            LOGGER.fine("Attempting to show error alert dialog");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Application Error");
            alert.setHeaderText("Failed to load application");

            String errorMessage = e.getMessage() != null ? e.getMessage() : "Unknown error";
            alert.setContentText("Error: " + errorMessage + "\nPlease check the logs.");

            alert.showAndWait();
            LOGGER.info("Error alert displayed to user");
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Critical failure - could not show error dialog", ex);
        }
    }

    public static void main(String[] args) {
        LOGGER.config(() -> "Launching JavaFX application with args: " + Arrays.toString(args));
        try {
            Thread.setDefaultUncaughtExceptionHandler((thread, throwable) -> {
                LOGGER.log(Level.SEVERE, "Uncaught exception in thread " + thread.getName(), throwable);
            });

            launch(args);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Fatal error during application launch", e);
            System.exit(1);
        }
    }
}