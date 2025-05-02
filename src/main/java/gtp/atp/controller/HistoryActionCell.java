package gtp.atp.controller;

import gtp.atp.model.RegexHistory;
import gtp.atp.service.RegexHistoryManager;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import java.util.function.Consumer;

/**
 * Custom TableCell implementation for displaying action buttons (Use/Delete)
 * in a TableView of regex history entries. Handles user interactions with history items.
 */
public class HistoryActionCell extends TableCell<RegexHistory, String> {
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
                patternConsumer.accept(entry.getPattern());
                closeHistoryWindow();
            }
        });

        // Set up Delete button action
        deleteButton.setOnAction(event -> {
            RegexHistory entry = getCurrentEntry();
            if (entry != null) {
                confirmAndDelete(historyManager, refreshCallback, entry);
            }
        });
    }

    /**
     * Gets the current RegexHistory entry associated with this table row.
     *
     * @return the current RegexHistory entry, or null if not available
     */
    private RegexHistory getCurrentEntry() {
        return (RegexHistory) getTableRow().getItem();
    }

    /**
     * Closes the history window containing this table view.
     */
    private void closeHistoryWindow() {
        getTableView().getScene().getWindow().hide();
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
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Pattern");
        alert.setHeaderText("Delete Regex Pattern");
        alert.setContentText("Are you sure you want to delete: " + entry.getPattern() + "?");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                historyManager.removeRegexHistory(entry.getPattern());
                refreshCallback.run();
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
            setGraphic(null);
        } else {
            setGraphic(buttonBox);
        }
    }
}