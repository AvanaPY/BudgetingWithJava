package org.sture.java.budgeting.controller.statusbar;

import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.HBox;

import java.util.UUID;

@SuppressWarnings("ClassCanBeRecord")
public class ProgressionRecord{
    public final UUID uuid;
    public final HBox vbox;
    public final ProgressIndicator progressIndicator;
    public final Label label;
    public ProgressionRecord(
            UUID uuid,
            HBox vbox,
            ProgressIndicator progressIndicator,
            Label label) {
        this.uuid = uuid;
        this.vbox = vbox;
        this.progressIndicator = progressIndicator;
        this.label = label;
    }
}
