package com.we.suck.at.java.budgettingiguess;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class HelloApplication extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        System.out.println(System.getProperty("javafx.version"));
        System.out.println(System.getProperty("java.version"));
        Group root = new Group();

        Parent helloView = FXMLLoader.load(getClass().getResource("hello-view.fxml"));

        root.getChildren().add(helloView);

        Scene scene = new Scene(root);
        stage.setScene(scene);

        Image image = new Image("budget-icon.png");
        stage.getIcons().add(image);

        Circle circle = new Circle();
        circle.setCenterX(stage.getWidth() / 2);
        circle.setCenterY(stage.getHeight() / 2);
        circle.setRadius(100);
        circle.setFill(Color.LIGHTBLUE);

        stage.show();
    }
}