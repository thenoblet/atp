package gtp.atp.controller;

import gtp.atp.exception.InvalidRegexException;
import gtp.atp.service.RegexHistoryManager;
import gtp.atp.util.ControllerUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javafx.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import gtp.atp.service.RegexProcessor;

/**
 * Controller class for handling the main data flow operations in the application.
 * Manages text input/output, regex pattern matching, and file operations.
 */
public class DataFlowController {
    private static final Logger LOGGER = Logger.getLogger(DataFlowController.class.getName());

    private RegexProcessor regexProcessor;
    private final RegexHistoryManager historyManager = new RegexHistoryManager();
    private Scene mainScene; // Store reference to main scene
    private Parent mainRoot;

    @FXML
    private TextArea inputTextArea;

    @FXML
    private TextArea outputTextArea;

    @FXML
    private TextField regexPatternField;

    @FXML
    private TextField replacementTextField;

    @FXML
    private Button uploadButton;

    /**
     * Sets the main root view for navigation purposes.
     *
     * @param root the Parent node of the main view
     */
    public void setMainRoot(Parent root) {
        LOGGER.fine("Setting main root view for navigation");
        this.mainRoot = root;
        this.mainScene = root.getScene();
    }

    /**
     * Handles file upload action triggered by the upload button.
     * Opens a file chooser dialog and loads the selected file's content into the input text area.
     */
    @FXML
    private void handleFileUpload() {
        LOGGER.fine("Initiating file upload process");
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Text File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Text Files", "*.txt"),
                new FileChooser.ExtensionFilter("All Files", "*.*")
        );

        Stage stage = (Stage) uploadButton.getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile != null) {
            try {
                LOGGER.info("Attempting to read file: " + selectedFile.getPath());
                String content = new String(Files.readAllBytes(Paths.get(selectedFile.getPath())));
                inputTextArea.setText(content);
                LOGGER.info("File loaded successfully. Character count: " + content.length());
            } catch (IOException e) {
                String errorMsg = "Error loading file: " + selectedFile.getPath();
                LOGGER.log(Level.SEVERE, errorMsg, e);
                inputTextArea.setText(errorMsg);
            }
        } else {
            LOGGER.fine("File selection was cancelled by user");
        }
    }

    /**
     * Handles the search action using the provided regex pattern.
     * Validates input, processes the regex matching, and displays results.
     */
    @FXML
    private void handleSearch() {
        LOGGER.fine("Initiating regex search operation");
        String inputText = inputTextArea.getText();
        String regexPattern = regexPatternField.getText();

        if (inputText.isBlank()) {
            LOGGER.warning("Search attempted with empty input text");
            ControllerUtils.showAlert("Text Field Empty", "Please enter text input");
            return;
        }

        if (regexPattern.isBlank()) {
            LOGGER.warning("Search attempted with empty regex pattern");
            ControllerUtils.showAlert("Regex Pattern Empty", "Please enter regex pattern");
            return;
        }

        LOGGER.config("Processing regex search with pattern: " + regexPattern);
        regexProcessor = new RegexProcessor(regexPattern, historyManager);

        List<String> regexMatches = regexProcessor.findMatchesAndRecord(inputText);

        if (regexMatches == null || regexMatches.isEmpty()) {
            LOGGER.info("No matches found for pattern: " + regexPattern);
            ControllerUtils.showAlert("No Matches", "No matches found for the given pattern");
            return;
        }

        LOGGER.info("Found " + regexMatches.size() + " matches for pattern: " + regexPattern);
        outputTextArea.setText(String.join("\n", regexMatches));
    }

    /**
     * Handles the replacement action using the provided regex pattern and replacement text.
     * Validates all input fields before processing.
     */
    @FXML
    private void handleReplace()  {
        LOGGER.fine("Initiating regex replace operation");
        String inputText = inputTextArea.getText();
        String regexPattern = regexPatternField.getText();
        String replacement = replacementTextField.getText();

        if (inputText.isBlank()) {
            LOGGER.warning("Replace attempted with empty input text");
            ControllerUtils.showAlert("Text Field Empty", "Please enter text input");
            return;
        }

        if (regexPattern.isBlank()) {
            LOGGER.warning("Replace attempted with empty regex pattern");
            ControllerUtils.showAlert("Regex Pattern Empty", "Please enter regex pattern");
            return;
        }

        if (replacement.isBlank()) {
            LOGGER.warning("Replace attempted with empty replacement text");
            ControllerUtils.showAlert("Replacement Pattern Empty", "Please enter replacement pattern");
            return;
        }

        LOGGER.config(String.format(
                "Processing regex replace - Pattern: %s, Replacement: %s",
                regexPattern, replacement));

        regexProcessor = new RegexProcessor(regexPattern, historyManager);

        String newText = regexProcessor.findAndReplace(inputText, replacement);

        if (newText != null) {
            LOGGER.info("Replace operation completed successfully");
            outputTextArea.setText(newText);
        }
    }

    /**
     * Handles saving the output text to a file.
     * Opens a file chooser dialog for selecting the save location.
     */
    @FXML
    private void handleSave() {
        LOGGER.fine("Initiating save operation");
        if (outputTextArea.getText().isBlank()) {
            LOGGER.warning("Save attempted with empty output");
            ControllerUtils.showAlert("Empty Output", "No content to save");
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Output");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Text Files", "*.txt")
        );

        Stage stage = (Stage) uploadButton.getScene().getWindow();
        File file = fileChooser.showSaveDialog(stage);

        if (file != null) {
            try {
                LOGGER.info("Attempting to save to file: " + file.getPath());
                Files.write(Paths.get(file.getPath()), outputTextArea.getText().getBytes());
                LOGGER.info("File saved successfully");
            } catch (IOException e) {
                String errorMsg = "Error saving file: " + file.getPath();
                LOGGER.log(Level.SEVERE, errorMsg, e);
                outputTextArea.setText(errorMsg);
            }
        } else {
            LOGGER.fine("Save operation cancelled by user");
        }
    }

    /**
     * Handles exporting the output (currently same as save).
     * Can be extended to support different formats in the future.
     */
    @FXML
    private void handleExport() {
        LOGGER.fine("Initiating export operation");
        handleSave();
    }

    /**
     * Clears both input and output text areas.
     */
    @FXML
    private void handleClear() {
        LOGGER.fine("Clearing input and output fields");
        inputTextArea.clear();
        outputTextArea.clear();
    }

    /**
     * Shows the regex history view and sets up the callback for pattern selection.
     *
     * @param event the action event that triggered this method
     */
    @FXML
    private void showRegexHistory(ActionEvent event) {
        LOGGER.fine("Loading regex history view");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/atp/view/regexhistory.fxml"));
            Parent historyView = loader.load();

            RegexHistoryController historyController = loader.getController();
            historyController.setHistoryManager(historyManager);

            historyController.setPatternConsumer(pattern -> {
                LOGGER.info("Selected pattern from history: " + pattern);
                regexPatternField.setText(pattern);
                returnToMainView(event);
            });

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(historyView);
            stage.setScene(scene);
            LOGGER.info("Regex history view displayed");

        } catch (IOException e) {
            String errorMsg = "Failed to load history view";
            LOGGER.log(Level.SEVERE, errorMsg, e);
            ControllerUtils.showAlert("Error", errorMsg + ": " + e.getMessage());
        }
    }

    /**
     * Returns to the main view from the history view.
     *
     * @param event the action event that triggered this method
     */
    private void returnToMainView(ActionEvent event) {
        LOGGER.fine("Returning to main view from history");
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(mainScene);
    }
}