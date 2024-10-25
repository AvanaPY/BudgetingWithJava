package org.sture.java.budgeting.controller.settings;

import javafx.scene.Scene;
import javafx.stage.Stage;
import org.sture.java.budgeting.SettingsController;

@SuppressWarnings("ClassCanBeRecord")
public class SettingsControllerScenePackage {
    public final Stage stage;
    public final Scene scene;
    public final SettingsController controller;

    public SettingsControllerScenePackage(Stage stage, Scene scene, SettingsController controller) {
        this.stage = stage;
        this.scene = scene;
        this.controller = controller;
    }
}
