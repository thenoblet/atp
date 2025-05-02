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

/**
 * Controller for managing and displaying the history of used regular expression patterns.
 * Provides functionality to view, search, and select previously used patterns.
 */
public class RegexHistoryController {
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
        this.historyManager = historyManager;
        loadHistoryData();
    }

    /**
     * Sets the callback for when a pattern is selected from history.
     *
     * @param patternConsumer callback that receives the selected pattern
     */
    public void setPatternConsumer(Consumer<String> patternConsumer) {
        this.patternConsumer = patternConsumer;
    }

    /**
     * Initializes the controller and sets up table columns and event handlers.
     * Called automatically after FXML loading.
     */
    @FXML
    private void initialize() {
        configurePatternColumn();
        configureUsageCountColumn();
        configureLastUsedColumn();
        configureActionsColumn();
        setupSearchFunctionality();
    }

    /**
     * Configures the pattern column with cell value factory.
     */
    private void configurePatternColumn() {
        patternColumn.setCellValueFactory(new PropertyValueFactory<>("pattern"));
    }

    /**
     * Configures the usage count column with cell value factory.
     */
    private void configureUsageCountColumn() {
        usageCountColumn.setCellValueFactory(new PropertyValueFactory<>("usageCount"));
    }

    /**
     * Configures the last used column with formatted date display.
     */
    private void configureLastUsedColumn() {
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
                    LocalDateTime date = LocalDateTime.parse(item);
                    setText(date.format(dateFormatter));
                }
            }
        });
    }

    /**
     * Configures the actions column with custom cell factory.
     */
    private void configureActionsColumn() {
        actionsColumn.setCellFactory(col -> new HistoryActionCell(
                historyManager,
                this::refreshHistory,
                this::handlePatternSelection
        ));
    }

    /**
     * Sets up search functionality with text field listener.
     */
    private void setupSearchFunctionality() {
        historyTable.setItems(historyData);
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filterHistory(newValue);
        });
    }

    /**
     * Loads history data from the history manager.
     */
    private void loadHistoryData() {
        if (historyManager != null) {
            historyData.setAll(historyManager.getRegexHistoryList());
        }
    }

    /**
     * Filters history entries based on search term.
     *
     * @param searchTerm the term to filter patterns by
     */
    private void filterHistory(String searchTerm) {
        if (searchTerm == null || searchTerm.isEmpty()) {
            loadHistoryData();
        } else {
            historyData.setAll(historyManager.searchPatterns(searchTerm));
        }
    }

    /**
     * Handles pattern selection from the history table.
     *
     * @param pattern the selected pattern
     */
    private void handlePatternSelection(String pattern) {
        if (patternConsumer != null) {
            patternConsumer.accept(pattern);
        }
    }

    /**
     * Refreshes the history data in the table.
     */
    private void refreshHistory() {
        List<RegexHistory> allHistory = historyManager.getRegexHistoryList();
        historyData.setAll(allHistory);
    }

    /**
     * Handles the back button action to return to main view.
     *
     * @param event the action event
     */
    @FXML
    private void handleBack(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/atp/view/mainview.fxml"));
            Parent mainView = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(mainView));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}