package gtp.atp.controller;

import gtp.atp.model.RegexHistory;
import gtp.atp.service.RegexHistoryManager;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;

import java.awt.desktop.SystemEventListener;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.function.Consumer;

/**
 * Custom TableCell implementation for displaying action buttons (Use/Delete)
 * in a TableView of regex history entries. Handles user interactions with history items.
 */
public class HistoryActionCell extends TableCell<RegexHistory, String> {
    private static final Logger LOGGER = Logger.getLogger(HistoryActionCell.class.getName());

    private final Button useButton = new Button("Use");
    private final Button deleteButton = new Button("Delete");
    private final HBox buttonBox = new HBox(5, useButton, deleteButton);

    /**
     * Constructs a new HistoryActionCell with the specified dependencies.
     *
     * @param historyManager the manager handling regex history operations
     * @param refreshCallback callback to refresh the history view after changes
     * @param patternConsumer callback to handle pattern selection
     */
    public HistoryActionCell(RegexHistoryManager historyManager,
                             Runnable refreshCallback,
                             Consumer<String> patternConsumer) {

        // Configure button styles
        useButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        deleteButton.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;");

        // Set up Use button action
        useButton.setOnAction(event -> {
            RegexHistory entry = getCurrentEntry();
            if (entry != null) {
                LOGGER.info("User selected pattern from history: " + entry.getPattern());
                patternConsumer.accept(entry.getPattern());
                closeHistoryWindow();
            } else {
                LOGGER.warning("Attempted to use null history entry");
            }
        });

        // Set up Delete button action
        deleteButton.setOnAction(event -> {
            RegexHistory entry = getCurrentEntry();
            if (entry != null) {
                LOGGER.fine("User initiated delete for pattern: " + entry.getPattern());
                confirmAndDelete(historyManager, refreshCallback, entry);
            } else {
                LOGGER.warning("Attempted to delete null history entry");
            }
        });

        LOGGER.fine("HistoryActionCell initialized with action buttons");
    }

    /**
     * Gets the current RegexHistory entry associated with this table row.
     *
     * @return the current RegexHistory entry, or null if not available
     */
    private RegexHistory getCurrentEntry() {
        RegexHistory entry = (RegexHistory) getTableRow().getItem();
        if (entry == null) {
            LOGGER.finest("Current table row has no associated history entry");
        }
        return entry;
    }

    /**
     * Closes the history window containing this table view.
     */
    private void closeHistoryWindow() {
        try {
            getTableView().getScene().getWindow().hide();
            LOGGER.fine("History window closed after pattern selection");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to close history window", e);
        }
    }

    /**
     * Shows a confirmation dialog and deletes the entry if confirmed.
     *
     * @param historyManager the history manager to perform deletion
     * @param refreshCallback callback to refresh the view after deletion
     * @param entry the history entry to delete
     */
    private void confirmAndDelete(RegexHistoryManager historyManager,
                                  Runnable refreshCallback,
                                  RegexHistory entry) {
        String pattern = entry.getPattern();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Pattern");
        alert.setHeaderText("Delete Regex Pattern");
        alert.setContentText("Are you sure you want to delete: " + pattern + "?");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    LOGGER.info("Deleting pattern from history: " + pattern);
                    RegexHistory removed = historyManager.removeRegexHistory(pattern);
                    if (removed.getPattern().equalsIgnoreCase(pattern)) {
                        LOGGER.fine("Pattern successfully deleted: " + pattern);
                        refreshCallback.run();
                    } else {
                        LOGGER.warning("Pattern not found in history: " + pattern);
                    }
                } catch (Exception e) {
                    LOGGER.log(Level.SEVERE, "Failed to delete pattern: " + pattern, e);
                }
            } else {
                LOGGER.fine("User cancelled deletion of pattern: " + pattern);
            }
        });
    }

    /**
     * Updates the cell content based on the current item state.
     *
     * @param item the string representation of the item (not used directly)
     * @param empty whether the cell represents empty data
     */
    @Override
    protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
            LOGGER.finest("Rendering empty history cell");
            setGraphic(null);
        } else {
            LOGGER.finest("Rendering history cell with action buttons");
            setGraphic(buttonBox);
        }
    }
}