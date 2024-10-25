package org.sture.java.budgeting;

import javafx.scene.layout.BorderPane;
import org.sture.java.budgeting.controller.statusbar.IStatusBarController;
import org.sture.java.budgeting.di.DiContainer;
import org.sture.java.budgeting.di.RegisterType;
import org.sture.java.budgeting.services.job.BackgroundJobExecutionService;
import org.sture.java.budgeting.store.dto.TrackingEntryDTOConverter;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.sture.java.budgeting.services.BudgetTypeCategoryProvider;
import org.sture.java.budgeting.services.DirectoryFileProvider;
import org.sture.java.budgeting.store.TrackingEntryStoreService;
import org.sture.java.budgeting.services.tracking.TrackingService;

import java.io.IOException;
import java.util.Objects;

public class BudgetApplication extends Application {
    private static DiContainer container;
    public static void main(String[] args) {
        container = new DiContainer();

        container.Register(BackgroundJobExecutionService.class, RegisterType.Singleton);

        container.Register(DirectoryFileProvider.class, RegisterType.Singleton);
        container.Register(BudgetTypeCategoryProvider.class, RegisterType.Singleton);

        container.Register(TrackingEntryDTOConverter.class, RegisterType.Singleton);
        container.Register(TrackingEntryStoreService.class, RegisterType.Singleton);
        container.Register(TrackingService.class, RegisterType.Singleton);

        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Scene scene = MainSceneFactory(container);
        stage.setScene(scene);

        Image image = new Image("budget-icon.png");
        stage.getIcons().add(image);

        stage.show();
    }

    private <T> T LoadFXML(String resourceName) throws IOException {
        return FXMLLoader.load(Objects.requireNonNull(getClass().getResource(resourceName)));
    }

    private Scene MainSceneFactory(DiContainer container) throws IOException {
        Group root = new Group();

        FXMLLoader statusBarViewLoader = new FXMLLoader(getClass().getResource("status-bar.fxml"));
        Parent statusBarView = statusBarViewLoader.load();
        StatusBarController statusBarController = statusBarViewLoader.getController();
        container.Register(IStatusBarController.class, statusBarController);

        FXMLLoader homeViewLoader = new FXMLLoader(getClass().getResource("hello-view.fxml"));
        Parent homeView = homeViewLoader.load();
        HomeController homeController = homeViewLoader.getController();
        container.Register(HomeController.class, homeController);

        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(homeView);
        borderPane.setBottom(statusBarView);

        root.getChildren().add(borderPane);

        return new Scene(root);
    }

    @SuppressWarnings("EmptyMethod")
    @Override
    public void stop() throws Exception {
        super.stop();
    }

    public static DiContainer getContainer(){
        return container;
    }
}