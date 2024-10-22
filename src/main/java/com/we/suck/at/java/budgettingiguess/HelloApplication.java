package com.we.suck.at.java.budgettingiguess;

import com.we.suck.at.java.budgettingiguess.di.DiContainer;
import com.we.suck.at.java.budgettingiguess.di.RegisterType;
import com.we.suck.at.java.budgettingiguess.dto.TrackingEntryDTOConverter;
import com.we.suck.at.java.budgettingiguess.services.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.Objects;

public class HelloApplication extends Application {
    private static DiContainer container;
    public static void main(String[] args) {
        container = new DiContainer();
        container.Register(DirectoryFileProvider.class, RegisterType.Singleton);
        container.Register(CategoryProvider.class, RegisterType.Singleton);
        container.Register(TrackingEntryDTOConverter.class, RegisterType.Singleton);
        container.Register(TrackingEntryStoreService.class, RegisterType.Singleton);
        container.Register(TrackingService.class, RegisterType.Singleton);

        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        System.out.println(System.getProperty("javafx.version"));
        System.out.println(System.getProperty("java.version"));
        Group root = new Group();

        Parent helloView = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("hello-view.fxml")));

        root.getChildren().add(helloView);

        Scene scene = new Scene(root);
        stage.setScene(scene);

        Image image = new Image("budget-icon.png");
        stage.getIcons().add(image);

        stage.show();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
    }

    public static DiContainer getContainer(){
        return container;
    }
}