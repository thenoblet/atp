package gtp.atp.util;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/**
 * Utility class for common controller operations with user feedback via alerts.
 */
public final class ControllerUtils {
    private ControllerUtils() {
        // Private constructor to prevent instantiation
    }

    /**
     * Displays an error alert with the given title and message.
     * @param title the alert title
     * @param message the alert content message
     */
    public static void showAlert(String title, String message) {
        showAlert(AlertType.ERROR, title, null, message);
    }

    /**
     * Displays a customizable alert dialog.
     * @param type the alert type (ERROR, WARNING, etc.)
     * @param title the alert title
     * @param header the header text (null for no header)
     * @param message the content message
     */
    public static void showAlert(AlertType type, String title, String header, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.showAndWait();
    }
}