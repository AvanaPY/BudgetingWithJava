package org.sture.java.budgeting.controller.settings;

import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.sture.java.budgeting.SettingsController;
import org.sture.java.budgeting.di.DiContainer;

import java.io.IOException;

public class SettingsSceneFactory {
    private static SettingsControllerScenePackage p;

    public static SettingsControllerScenePackage Build(Class<?> applicationClazz, DiContainer container)  {
        if(p != null)
            return p;

        try {
            Group root = new Group();

            FXMLLoader settingsSceneLoader = new FXMLLoader(applicationClazz.getResource("settings-scene.fxml"));
            Parent settingsScene = settingsSceneLoader.load();
            SettingsController controller = settingsSceneLoader.getController();
            container.Register(SettingsController.class, controller);

            root.getChildren().add(settingsScene);
            Scene scene = new Scene(root);

            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setResizable(false);
            stage.setTitle("Settings");

            p = new SettingsControllerScenePackage(stage, scene, controller);
            return p;
        } catch (IOException e) {
            throw new RuntimeException("I/O Error: " + e);
        }
    }
}