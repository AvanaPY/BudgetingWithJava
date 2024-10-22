package org.sture.java.budgeting;

import org.sture.java.budgeting.models.BudgetType;
import org.sture.java.budgeting.services.DownloadService;
import org.sture.java.budgeting.services.TrackingService;
import org.sture.java.budgeting.utils.ProgressOperator;
import org.sture.java.budgeting.utils.Utils;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.*;

import java.time.LocalDate;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import org.sture.java.budgeting.models.TrackingEntry;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;

@SuppressWarnings({"ClassEscapesDefinedScope", "SameParameterValue"})
public class HelloController {

    // Progression bar in status bar
    @FXML Label progressionLabel;
    @FXML ProgressBar progressBar;

    // Widgets for displaying data in our table view
    @FXML TableView<TrackingEntry> trackingTableView;
    @FXML TableColumn<TrackingEntry, LocalDate> dateColumn;
    @FXML TableColumn<TrackingEntry, LocalDate> effectiveDateColumn;
    @FXML TableColumn<TrackingEntry, String> typeColumn;
    @FXML TableColumn<TrackingEntry, String> detailsColumn;
    @FXML TableColumn<TrackingEntry, String> categoryColumn;
    @FXML TableColumn<TrackingEntry, Double> amountColumn;
    @FXML TableColumn<TrackingEntry, Double> balanceColumn;

    // Widgets for adding new data
    @FXML ComboBox<BudgetType> budgetTypeComboBox;
    @FXML ComboBox<String> categoryComboBox;
    @FXML DatePicker datePicker;
    @FXML TextField amountTextField;
    @FXML TextArea detailsTextArea;
    @FXML Button submitEntryDataButton;

    // Private
    private TrackingService trackingService;
    private String lastProgressText;
    private TrackingEntry selectedTrackingEntry;

    public void initialize() {
        trackingService = HelloApplication.getContainer().ResolveInstance(TrackingService.class);
        trackingService.initialize();

        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        effectiveDateColumn.setCellValueFactory(new PropertyValueFactory<>("effectiveDate"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        detailsColumn.setCellValueFactory(new PropertyValueFactory<>("details"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        balanceColumn.setCellValueFactory(new PropertyValueFactory<>("balance"));

        trackingTableView.setItems(trackingService.GetTrackingEntryList());
        trackingTableView.getSelectionModel().selectedItemProperty().addListener(
                (observableValue, _, _) -> selectTrackingEntry(observableValue.getValue()));

        datePicker.setValue(LocalDate.now());
        initializeComboBoxes();
        updateCategoryComboBoxFromTypeComboBoxActiveType();
    }

    private void initializeComboBoxes() {
        budgetTypeComboBox.setItems(trackingService.GetBudgetTypeList());
        budgetTypeComboBox.setValue(BudgetType.Income);

        categoryComboBox.setItems(trackingService.GetCategoryList());
        categoryComboBox.setValue(trackingService.GetCategoryList().getFirst());
    }

    private void updateCategoryComboBoxFromTypeComboBoxActiveType() {
        BudgetType budgetType = budgetTypeComboBox.getValue();
        trackingService.UpdateCategories(budgetType);

        categoryComboBox.setValue(trackingService.GetCategoryList().getFirst());
    }

    public void startDownload() {
        onStartDownload();
    }

    public void onDownloadComplete() {
        progressBar.setCursor(Cursor.DEFAULT);
        updateProgressStatus("Complete", 1d);
        lastProgressText = "";
        System.out.println("Finished");
    }

    private void onStartDownload() {
        try {
            progressBar.setCursor(Cursor.WAIT);
            DownloadService.InitializeDownload(new ProgressOperator() {
                @Override
                public void OnStartProgress(){
                    setProgressionVisible(true);
                    lastProgressText = "";
                    updateProgressStatus("Starting...", 0);
                    try {
                        TimeUnit.MILLISECONDS.sleep(400);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }

                @Override
                public void OnProgressUpdate(String status, double progress) {
                    updateProgressStatus(status, progress);
                }

                @Override
                public void OnProgressComplete() {
                    onDownloadComplete();
                }
            });
        } catch (Exception _) {}
    }

    private void updateProgressStatus(String progressText, double progression){
        if(!Objects.equals(progressText, lastProgressText))
        {
            lastProgressText = progressText;
            Platform.runLater(() -> progressionLabel.setText(progressText));
        }
        progressBar.setProgress(progression);
    }

    private void setProgressionVisible(boolean visible) {
        progressBar.setVisible(visible);
        progressionLabel.setVisible(visible);
    }

    public void buttonAddNewEntry() {
        LocalDate date          = datePicker.getValue();
        BudgetType budgetType   = budgetTypeComboBox.getValue();
        String category         = categoryComboBox.getValue();
        String details          = detailsTextArea.getText();
        Double amount           = Utils.parseTextAsDoubleOrNull(amountTextField.getText());

        trackingService.addNewEntry(date, budgetType, category, details, amount);
        forceRefresh();
    }

    private void forceRefresh() {
        /* ugly workaround because the TableView doesn't update all the rows in the UI
        which may have been updated after we called on addNewEntry
        but as the cool kids say, it is what it is 8) */

        // Store index of selection first
        var selectedIndex = trackingTableView.getSelectionModel().getSelectedIndex();

        // Reset items for TableView
        trackingTableView.setItems(null);
        trackingTableView.layout();
        trackingTableView.setItems(trackingService.GetTrackingEntryList());

        // Restore selection
        trackingTableView.getSelectionModel().selectIndices(selectedIndex);
    }

    public void buttonDatePickerIncrementer() {
        datePicker.setValue(datePicker.getValue().plusDays(1));
    }

    public void buttonDatePickerDecrementer() {
        datePicker.setValue(datePicker.getValue().minusDays(1));
    }

    public void budgetTypeComboBoxOnAction() {
        updateCategoryComboBoxFromTypeComboBoxActiveType();
    }

    public void selectTrackingEntry(TrackingEntry entry) {
        if(entry == null)
            return;

        System.out.println("Selected " + entry);

        selectedTrackingEntry = entry;
        submitEntryDataButton.setText("Update selected");

        datePicker.setValue(selectedTrackingEntry.getDate());
        budgetTypeComboBox.setValue(selectedTrackingEntry.getType());
        categoryComboBox.setValue(selectedTrackingEntry.getCategory());
        amountTextField.setText(String.valueOf(selectedTrackingEntry.getAmount()));
        detailsTextArea.setText(selectedTrackingEntry.getDetails());
    }

    public void tableViewOnKeyPressed(KeyEvent event){
        System.out.println(event);
        if(event.getEventType() == KeyEvent.KEY_PRESSED){
            switch(event.getCode()){
                case ESCAPE -> clearSelection();
                case DELETE -> {
                    try {
                        clearSelection();
                        trackingTableView.getSelectionModel().clearSelection();
                        trackingService.DeleteTrackingEntry(selectedTrackingEntry);
                        forceRefresh();
                    } catch (Exception e) {
                        System.out.println("Failed to delete entry: " + e.getMessage());
                    }
                }
                default -> {}

            }
        }
    }

    public void clearSelection() {
        // This can be called from a different thread (i.e. when handling keyboard events I guess?) so wrap it in a runnable
        Platform.runLater(() -> {
            selectedTrackingEntry = null;
            submitEntryDataButton.setText("Submit");
            trackingTableView.getSelectionModel().clearSelection();

            datePicker.setValue(LocalDate.now());
            budgetTypeComboBox.setValue(trackingService.GetBudgetTypeList().getFirst());
            categoryComboBox.setValue(trackingService.GetCategoryList().getFirst());
            amountTextField.setText("");
            detailsTextArea.setText("");
        });
    }

    @FXML
    public void exitApplication(ActionEvent event){
        System.out.println("owo");
    }
}