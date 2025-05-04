package gtp.atp.controller;

import gtp.atp.model.RegexHistory;
import gtp.atp.service.RegexHistoryManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Controller for managing and displaying the history of used regular expression patterns.
 * Provides functionality to view, search, and select previously used patterns.
 */
public class RegexHistoryController {
    private static final Logger LOGGER = Logger.getLogger(RegexHistoryController.class.getName());

    @FXML private TableView<RegexHistory> historyTable;
    @FXML private TableColumn<RegexHistory, String> patternColumn;
    @FXML private TableColumn<RegexHistory, Number> usageCountColumn;
    @FXML private TableColumn<RegexHistory, String> lastUsedColumn;
    @FXML private TableColumn<RegexHistory, String> actionsColumn;
    @FXML private TextField searchField;

    private final ObservableList<RegexHistory> historyData = FXCollections.observableArrayList();
    private RegexHistoryManager historyManager;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm");
    private Consumer<String> patternConsumer;

    /**
     * Sets the history manager and loads initial history data.
     *
     * @param historyManager the history manager instance to use
     */
    public void setHistoryManager(RegexHistoryManager historyManager) {
        LOGGER.fine("Setting history manager and loading initial data");
        this.historyManager = historyManager;
        loadHistoryData();
    }

    /**
     * Sets the callback for when a pattern is selected from history.
     *
     * @param patternConsumer callback that receives the selected pattern
     */
    public void setPatternConsumer(Consumer<String> patternConsumer) {
        LOGGER.fine("Setting pattern selection consumer");
        this.patternConsumer = patternConsumer;
    }

    /**
     * Initializes the controller and sets up table columns and event handlers.
     * Called automatically after FXML loading.
     */
    @FXML
    private void initialize() {
        LOGGER.fine("Initializing RegexHistoryController");
        try {
            configurePatternColumn();
            configureUsageCountColumn();
            configureLastUsedColumn();
            configureActionsColumn();
            setupSearchFunctionality();
            LOGGER.fine("RegexHistoryController initialized successfully");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to initialize RegexHistoryController", e);
            throw new RuntimeException("Controller initialization failed", e);
        }
    }

    private void configurePatternColumn() {
        LOGGER.finest("Configuring pattern column");
        patternColumn.setCellValueFactory(new PropertyValueFactory<>("pattern"));
    }

    private void configureUsageCountColumn() {
        LOGGER.finest("Configuring usage count column");
        usageCountColumn.setCellValueFactory(new PropertyValueFactory<>("usageCount"));
    }

    private void configureLastUsedColumn() {
        LOGGER.finest("Configuring last used column");
        lastUsedColumn.setCellValueFactory(cellData ->
                cellData.getValue().timestampProperty().asString()
        );

        lastUsedColumn.setCellFactory(column -> new TableCell<RegexHistory, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    try {
                        LocalDateTime date = LocalDateTime.parse(item);
                        setText(date.format(dateFormatter));
                    } catch (Exception e) {
                        LOGGER.log(Level.WARNING, "Failed to format date: " + item, e);
                        setText("Invalid date");
                    }
                }
            }
        });
    }

    private void configureActionsColumn() {
        LOGGER.finest("Configuring actions column");
        actionsColumn.setCellFactory(col -> {
            LOGGER.finest("Creating new HistoryActionCell");
            return new HistoryActionCell(
                    historyManager,
                    this::refreshHistory,
                    this::handlePatternSelection
            );
        });
    }

    private void setupSearchFunctionality() {
        LOGGER.fine("Setting up search functionality");
        historyTable.setItems(historyData);
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            LOGGER.fine("Search field changed: " + newValue);
            filterHistory(newValue);
        });
    }

    private void loadHistoryData() {
        if (historyManager != null) {
            LOGGER.fine("Loading history data from manager");
            try {
                List<RegexHistory> historyList = historyManager.getRegexHistoryList();
                historyData.setAll(historyList);
                LOGGER.info("Loaded " + historyList.size() + " history entries");
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Failed to load history data", e);
            }
        } else {
            LOGGER.warning("Attempted to load history data with null history manager");
        }
    }

    private void filterHistory(String searchTerm) {
        if (searchTerm == null || searchTerm.isEmpty()) {
            LOGGER.fine("Resetting history filter");
            loadHistoryData();
        } else {
            LOGGER.fine("Filtering history with search term: " + searchTerm);
            try {
                List<RegexHistory> filtered = historyManager.searchPatterns(searchTerm);
                historyData.setAll(filtered);
                LOGGER.fine("Filtered to " + filtered.size() + " matching entries");
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Failed to filter history", e);
            }
        }
    }

    private void handlePatternSelection(String pattern) {
        LOGGER.info("Pattern selected from history: " + pattern);
        if (patternConsumer != null) {
            patternConsumer.accept(pattern);
        } else {
            LOGGER.warning("No pattern consumer set for selection");
        }
    }

    private void refreshHistory() {
        LOGGER.fine("Refreshing history data");
        loadHistoryData();
    }

    @FXML
    private void handleBack(ActionEvent event) {
        LOGGER.fine("Handling back button action");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/atp/view/mainview.fxml"));
            Parent mainView = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(mainView));
            LOGGER.fine("Successfully navigated back to main view");
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to navigate back to main view", e);
        }
    }
}