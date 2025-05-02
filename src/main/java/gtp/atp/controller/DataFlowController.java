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

import gtp.atp.service.RegexProcessor;

/**
 * Controller class for handling the main data flow operations in the application.
 * Manages text input/output, regex pattern matching, and file operations.
 */
public class DataFlowController {

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
        this.mainRoot = root;
        this.mainScene = root.getScene();
    }

    /**
     * Handles file upload action triggered by the upload button.
     * Opens a file chooser dialog and loads the selected file's content into the input text area.
     */
    @FXML
    private void handleFileUpload() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Text File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Text Files", "*.txt"),
                new FileChooser.ExtensionFilter("All Files", "*.*")
        );

        // Get the stage from the button
        Stage stage = (Stage) uploadButton.getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile != null) {
            try {
                String content = new String(Files.readAllBytes(Paths.get(selectedFile.getPath())));
                inputTextArea.setText(content);
            } catch (IOException e) {
                inputTextArea.setText("Error loading file: " + e.getMessage());
            }
        }
    }

    /**
     * Handles the search action using the provided regex pattern.
     * Validates input, processes the regex matching, and displays results.
     */
    @FXML
    private void handleSearch() {
        String inputText = inputTextArea.getText();
        String regexPattern = regexPatternField.getText();

        if (inputText.isBlank()) {
            ControllerUtils.showAlert("Text Field Empty", "Please enter text input");
            return;
        }

        if (regexPattern.isBlank()) {
            ControllerUtils.showAlert("Regex Pattern Empty", "Please enter regex pattern");
            return;
        }

        regexProcessor = new RegexProcessor(regexPattern, historyManager);
        List<String> regexMatches = regexProcessor.findMatchesAndRecord(inputText);

        if (regexMatches == null || regexMatches.isEmpty()) {
            ControllerUtils.showAlert("No Matches", "No matches found for the given pattern");
            return;
        }

        outputTextArea.setText(String.join("\n", regexMatches));
    }

    /**
     * Handles the replacement action using the provided regex pattern and replacement text.
     * Validates all input fields before processing.
     */
    @FXML
    private void handleReplace() {
        String inputText = inputTextArea.getText();
        String regexPattern = regexPatternField.getText();
        String replacement = replacementTextField.getText();

        if (inputText.isBlank()) {
            ControllerUtils.showAlert("Text Field Empty", "Please enter text input");
            return;
        }

        if (regexPattern.isBlank()) {
            ControllerUtils.showAlert("Regex Pattern Empty", "Please enter regex pattern");
            return;
        }

        if (replacement.isBlank()) {
            ControllerUtils.showAlert("Replacement Pattern Empty", "Please enter replacement pattern");
            return;
        }

        regexProcessor = new RegexProcessor(regexPattern, historyManager);
        String newText = regexProcessor.findAndReplace(inputText, replacement);

        if (newText != null) {
            outputTextArea.setText(newText);
        }
    }

    /**
     * Handles saving the output text to a file.
     * Opens a file chooser dialog for selecting the save location.
     */
    @FXML
    private void handleSave() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Output");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Text Files", "*.txt")
        );

        Stage stage = (Stage) uploadButton.getScene().getWindow();
        File file = fileChooser.showSaveDialog(stage);

        if (file != null) {
            try {
                Files.write(Paths.get(file.getPath()), outputTextArea.getText().getBytes());
            } catch (IOException e) {
                outputTextArea.setText("Error saving file: " + e.getMessage());
            }
        }
    }

    /**
     * Handles exporting the output (currently same as save).
     * Can be extended to support different formats in the future.
     */
    @FXML
    private void handleExport() {
        // Similar to save but with different formats or processing
        handleSave();
    }

    /**
     * Clears both input and output text areas.
     */
    @FXML
    private void handleClear() {
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
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/atp/view/regexhistory.fxml"));
            Parent historyView = loader.load();

            RegexHistoryController historyController = loader.getController();
            historyController.setHistoryManager(historyManager);

            historyController.setPatternConsumer(pattern -> {
                regexPatternField.setText(pattern);
                returnToMainView(event); // Return to main view after selection
            });

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(historyView);
            stage.setScene(scene);

        } catch (IOException e) {
            e.printStackTrace();
            ControllerUtils.showAlert("Error", "Failed to load history view: " + e.getMessage());
        }
    }

    /**
     * Returns to the main view from the history view.
     *
     * @param event the action event that triggered this method
     */
    private void returnToMainView(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(mainScene); // Restore main scene
    }
}