package org.sture.java.budgeting;

import javafx.event.EventHandler;
import org.sture.java.budgeting.developer.Developer;
import org.sture.java.budgeting.services.job.Job;
import org.sture.java.budgeting.services.job.BackgroundJobExecutionService;
import org.sture.java.budgeting.services.tracking.models.BudgetEntryCategory;
import org.sture.java.budgeting.services.tracking.models.BudgetEntrySubCategory;
import org.sture.java.budgeting.services.tracking.TrackingService;
import org.sture.java.budgeting.utils.Utils;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;
import java.util.Random;
import java.util.UUID;

import org.sture.java.budgeting.services.tracking.models.TrackingEntry;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;

@SuppressWarnings({"ClassEscapesDefinedScope", "SameParameterValue"})
public class HomeController {
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
    @FXML DatePicker datePicker;
    @FXML ComboBox<BudgetEntryCategory> budgetEntryCategoryComboBox;
    @FXML ComboBox<BudgetEntrySubCategory> budgetEntrySubCategoryComboBox;
    @FXML TextField amountTextField;
    @FXML TextArea detailsTextArea;
    @FXML Button submitEntryDataButton;

    @FXML Button buttonIncrementDate;
    @FXML Button buttonDecrementDate;

    // Private
    private TrackingService trackingService;
    private String lastProgressText;
    private TrackingEntry selectedTrackingEntry;

    public void initialize() {
        trackingService = HelloApplication.getContainer().ResolveInstance(TrackingService.class);
        trackingService.initialize();

        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        effectiveDateColumn.setCellValueFactory(new PropertyValueFactory<>("effectiveDate"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("subCategory"));
        detailsColumn.setCellValueFactory(new PropertyValueFactory<>("details"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        balanceColumn.setCellValueFactory(new PropertyValueFactory<>("balance"));

        trackingTableView.setItems(trackingService.GetTrackingEntryList());
        trackingTableView.getSelectionModel().selectedItemProperty().addListener(
                (observableValue, _, _) -> selectTrackingEntry(observableValue.getValue()));

        var whenMakingNewEntryOnKeyPressedEventHandler = new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                switch(keyEvent.getCode()) {
                    case ENTER -> {
                        submitEntryDataButton.fire();
                        amountTextField.clear();
                        detailsTextArea.clear();
                        budgetEntryCategoryComboBox.requestFocus();
                    }
                    case LEFT -> buttonDecrementDate.fire();
                    case RIGHT -> buttonIncrementDate.fire();
                }
            }
        };

        budgetEntryCategoryComboBox.setOnKeyPressed(whenMakingNewEntryOnKeyPressedEventHandler);
        budgetEntrySubCategoryComboBox.setOnKeyPressed(whenMakingNewEntryOnKeyPressedEventHandler);
        detailsTextArea.setOnKeyPressed(whenMakingNewEntryOnKeyPressedEventHandler);
        amountTextField.setOnKeyPressed(whenMakingNewEntryOnKeyPressedEventHandler);

        datePicker.setValue(LocalDate.now());
        initializeComboBoxes();
        updateCategoryComboBoxFromTypeComboBoxActiveType();
    }

    public void buttonDownloadTestStatusBar(){
        HelloApplication.getContainer().ResolveInstance(
                BackgroundJobExecutionService.class).LaunchBackgroundJob(new DummyJob(UUID.randomUUID()));
    }

    private void initializeComboBoxes() {
        budgetEntryCategoryComboBox.setItems(trackingService.GetBudgetCategoryList());
        budgetEntryCategoryComboBox.setValue(trackingService.GetBudgetCategoryList().getFirst());

        budgetEntrySubCategoryComboBox.setItems(trackingService.GetBudgetSubCategoryList());
        budgetEntrySubCategoryComboBox.setValue(trackingService.GetBudgetSubCategoryList().getFirst());
    }

    private void updateCategoryComboBoxFromTypeComboBoxActiveType() {
        BudgetEntryCategory activeCategory = budgetEntryCategoryComboBox.getValue();
        trackingService.UpdateCategories(activeCategory);

        budgetEntrySubCategoryComboBox.setValue(trackingService.GetBudgetSubCategoryList().getFirst());
    }

    public void buttonAddNewEntry() {
        LocalDate date                      = datePicker.getValue();
        BudgetEntryCategory budgetCategory  = budgetEntryCategoryComboBox.getValue();
        BudgetEntrySubCategory subCategory  = budgetEntrySubCategoryComboBox.getValue();
        String details                      = detailsTextArea.getText();
        Double amount                       = Utils.parseTextAsDoubleOrNull(amountTextField.getText());

        trackingService.addNewEntry(date, budgetCategory, subCategory, details, amount);
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
        budgetEntryCategoryComboBox.setValue(selectedTrackingEntry.getCategory());
        budgetEntrySubCategoryComboBox.setValue(selectedTrackingEntry.getSubCategory());
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

    public void onKeyPressed(KeyEvent event) {
        Developer.DebugKeyEvent(event);
    }

    public void clearSelection() {
        // This can be called from a different thread (i.e. when handling keyboard events I guess?) so wrap it in a runnable
        Platform.runLater(() -> {
            selectedTrackingEntry = null;
            submitEntryDataButton.setText("Submit");
            trackingTableView.getSelectionModel().clearSelection();

            datePicker.setValue(LocalDate.now());
            budgetEntryCategoryComboBox.setValue(trackingService.GetBudgetCategoryList().getFirst());
            budgetEntrySubCategoryComboBox.setValue(trackingService.GetBudgetSubCategoryList().getFirst());
            amountTextField.setText("");
            detailsTextArea.setText("");
        });
    }

    @FXML
    public void exitApplication(ActionEvent event){
        System.out.println("owo");
    }

    private static class DummyJob extends Job {
        public DummyJob(UUID uuid) {
            super(uuid);
        }

        @Override
        public void Execute() {
            Random random = new Random();
            try {
                UpdateMessage("Opening file stream...");
                Thread.sleep(1000);
                UpdateMessage("Parsing data...");
                UpdateProgression(0);
                double prog = 0;
                while(prog < 0.95){
                    double randomProg = random.nextDouble(0.05);
                    prog += randomProg;

                    UpdateProgression(prog);
                    if(prog < 0.2)
                        UpdateMessage("Parsing data...");
                    else if(prog < 0.3)
                        UpdateMessage("Writing data...");
                    else if(prog < 0.8)
                        UpdateMessage("Verifying data...");
                    long delay = random.nextLong(
                            (long)(randomProg * 4000),
                            (long)(randomProg * 10000));
                    Thread.sleep(delay);
                }
                UpdateMessage("Closing stream");
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}