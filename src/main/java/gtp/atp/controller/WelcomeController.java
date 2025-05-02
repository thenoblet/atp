package gtp.atp.controller;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * Controller for the welcome/splash screen of the Text Processing Tool.
 * <p>
 * This controller manages the initial loading animation sequence and transitions
 * to the main application window. It provides visual feedback during system
 * initialization through a progress indicator and status messages.
 * </p>
 */
public class WelcomeController {
    private static final Logger LOGGER = Logger.getLogger(WelcomeController.class.getName());

    /**
     * The progress indicator that visually represents the loading progress.
     * Value ranges from 0.0 (0%) to 1.0 (100%).
     */
    @FXML
    private ProgressIndicator progressIndicator;

    /**
     * The label that displays the current loading status message.
     */
    @FXML
    private Label statusLabel;

    /**
     * Initialises the controller after the FXML fields have been injected.
     *
     * Starts a simulated loading sequence with three distinct phases:
     *
     *  - System initialization (30% progress)
     *  - Employee data loading (60% progress)
     *  - Completion (100% progress)
     *
     * Each phase lasts 1 second and updates both the progress indicator
     * and status messages
     */
    @FXML
    public void initialize() {
        LOGGER.entering(getClass().getSimpleName(), "initialize");

        try {
            // Initial loading phase (30% complete)
            statusLabel.setText("Initialising DataFlow ATP...");
            progressIndicator.setProgress(0.3);
            LOGGER.fine("Started initialisation phase (30%)");

            PauseTransition pause = new PauseTransition(Duration.seconds(2));
            pause.setOnFinished(e -> {
                progressIndicator.setProgress(0.6);
                statusLabel.setText("Loading components...");
                LOGGER.fine("Progressed to data loading phase (60%)");

                PauseTransition pause2 = new PauseTransition(Duration.seconds(1));
                pause2.setOnFinished(e2 -> {
                    progressIndicator.setProgress(1.0);
                    statusLabel.setText("Ready!");
                    LOGGER.fine("Completed all loading phases (100%)");
                    loadMainApplication(); // Transition to main application
                });
                pause2.play();
            });
            pause.play();

            LOGGER.exiting(getClass().getSimpleName(), "initialize");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error during welcome screen initialization", e);
            throw e;
        }
    }

    /**
     * Loads and displays the main application window.
     * This method:
     *   Closes the current welcome screen
     *   Loads the main application FXML file
     *   Creates a new stage for the main application
     *   Sets the window title and dimensions
     *   Displays the main application window
     *
     * If an error occurs during loading, the stack trace is printed to standard error.
     */
    private void loadMainApplication() {
        LOGGER.entering(getClass().getSimpleName(), "loadMainApplication");

        try {
            Stage stage = (Stage) progressIndicator.getScene().getWindow();
            stage.close();
            LOGGER.fine("Closed welcome screen stage");

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/atp/view/mainview.fxml")
            );
            LOGGER.config("Loading main application FXML from: /atp/view/mainview.fxml");

            loader.setController(null);

            Scene scene = new Scene(loader.load(), 1440, 970);
            LOGGER.fine(() -> String.format("Created main scene with dimensions %dx%d",
                    (int) scene.getWidth(), (int) scene.getHeight()));

            Stage mainStage = new Stage();
            mainStage.setTitle("Employee Management System");
            mainStage.setScene(scene);
            mainStage.show();

            LOGGER.info("Main application window displayed successfully");
            LOGGER.exiting(getClass().getSimpleName(), "loadMainApplication");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to load main application", e);
            throw new RuntimeException("Failed to load main application", e);
        }
    }
}