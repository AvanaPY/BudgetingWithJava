package org.sture.java.budgeting;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.fxml.FXML;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.HBox;
import javafx.scene.text.TextAlignment;

import java.util.HashMap;
import java.util.UUID;

public class StatusBarController implements IStatusBarController {
    @FXML private HBox statusBarProgressHbox;

    private HashMap<UUID, ProgressionRecord> ongoingProgressesMap;

    public void initialize() {
        ongoingProgressesMap = new HashMap<>();
    }

    public void SetProgressMessageTo(UUID uuid, String msg){
        ProgressionRecord record = ongoingProgressesMap.getOrDefault(uuid, null);
        if(record == null)
            return;
        Platform.runLater(() -> record.label.setText(msg));
    }

    public void SetProgressProgressionTo(UUID uuid, Double progress){
        ProgressionRecord record = ongoingProgressesMap.getOrDefault(uuid, null);
        if(record == null)
            return;
        Platform.runLater(() -> record.progressIndicator.setProgress(progress));
    }

    public void SetProgressStarted(UUID uuid) {
        ProgressionRecord record = ongoingProgressesMap.getOrDefault(uuid, null);
        if(record == null)
            return;
        Platform.runLater(() -> record.progressIndicator.setCursor(Cursor.WAIT));
    }

    public void SetProgressCompleted(UUID uuid, String msg) {
        ProgressionRecord record = ongoingProgressesMap.getOrDefault(uuid, null);
        if(record == null)
            return;
        Platform.runLater(() -> {
            record.progressIndicator.setCursor(Cursor.DEFAULT);
            record.progressIndicator.setProgress(1);
            if(!msg.isEmpty())
                record.label.setText(msg);
        });
    }

    public UUID CreateNewProgression() {
        UUID uuid = UUID.randomUUID();

        HBox box = new HBox();
        ProgressIndicator progressIndicator = new ProgressBar();
        progressIndicator.setMaxWidth(60);
        progressIndicator.setPadding(new Insets(5));

        Label label = new Label();
        label.setPrefWidth(100);
        label.setMaxWidth(100);
        label.setPadding(new Insets(5));
        label.setTextAlignment(TextAlignment.CENTER);
        label.setText("");

        ProgressionRecord record = new ProgressionRecord(uuid, box, progressIndicator, label);
        ongoingProgressesMap.put(uuid, record);

        box.getChildren().addAll(label, progressIndicator);

        statusBarProgressHbox.getChildren().addAll(box);

        return uuid;
    }

    public void DeleteProgressionAfterDelay(UUID jobId, double delaySeconds){
        new Thread(() -> {
            try {
                Thread.sleep((long)(delaySeconds * 1000));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            DeleteProgression(jobId);
        }).start();
    }

    public void DeleteProgression(UUID jobId){
        ProgressionRecord record = ongoingProgressesMap.getOrDefault(jobId, null);
        if(record == null) {
            return;
        }
        Platform.runLater(() -> statusBarProgressHbox.getChildren().remove(record.vbox));
        ongoingProgressesMap.remove(jobId);
    }
}

