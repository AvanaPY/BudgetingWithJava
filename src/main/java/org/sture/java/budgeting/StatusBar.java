package org.sture.java.budgeting;

import org.sture.java.budgeting.utils.Utils;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.ProgressIndicator;

public class StatusBar implements DownloadController {
    @FXML
    private ProgressIndicator progressIndicator;

    @Override
    public void startProgress()
    {
        progressIndicator.setCursor(Cursor.WAIT);
        progressIndicator.setProgress(0);
        Utils.StartProgressionThread(this);
    }

    @Override
    public void setCurrentProgress(double progress) {
        progressIndicator.setProgress(progress);
    }

    @Override
    public void onProgressComplete() {
        progressIndicator.setProgress(1);
        progressIndicator.setCursor(Cursor.DEFAULT);
    }
}
