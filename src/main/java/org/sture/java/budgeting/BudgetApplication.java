package org.sture.java.budgeting;

import javafx.scene.layout.BorderPane;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.sture.java.budgeting.controller.statusbar.IStatusBarController;
import org.sture.java.budgeting.di.DiContainer;
import org.sture.java.budgeting.modules.BaseModule;
import org.sture.java.budgeting.providers.ProviderModule;
import org.sture.java.budgeting.services.category.module.CategoryModule;
import org.sture.java.budgeting.services.job.module.BackgroundJobModule;
import org.sture.java.budgeting.services.tracking.module.TrackingModule;
import org.sture.java.budgeting.developer.Developer;

import java.io.IOException;
import java.util.Objects;

public class BudgetApplication extends Application {
    private static DiContainer container;
    public static void main(String[] args) {
        container = new DiContainer();

        BaseModule[] modules = new BaseModule[]{
                new BackgroundJobModule(),
                new ProviderModule(),
                new TrackingModule(),
                new CategoryModule()
        };

        for(BaseModule module : modules)
            module.RegisterModule(container);

        for(BaseModule module : modules)
            module.OnAllModulesLoaded(container);

        launch(args);
    }

    @Override
    public void start(Stage stage) {
        Scene scene = MainSceneFactory(container);

        stage.setScene(scene);
//        stage.setScene(SettingsSceneFactory.Build(getClass(), container).scene);

        Image image = new Image("budget-icon.png");
        stage.getIcons().add(image);

        stage.show();
    }

    private <T> T LoadFXML(String resourceName) throws IOException {
        return FXMLLoader.load(Objects.requireNonNull(getClass().getResource(resourceName)));
    }

    private Scene MainSceneFactory(DiContainer container) {
        Group root = new Group();

        Developer.DebugMessage("Loading FXML files...", true);

        // Status bar
        Developer.DebugMessage("status-bar.fxml", true);

        try {
            FXMLLoader statusBarViewLoader = new FXMLLoader(getClass().getResource("status-bar.fxml"));
            Parent statusBarView = statusBarViewLoader.load();
            StatusBarController statusBarController = statusBarViewLoader.getController();
            container.Register(IStatusBarController.class, statusBarController);

            Developer.DebugMessage("Loading status-bar.fxml: " + (statusBarView != null ? "success" : "failed"));
            Developer.DeindentDebugMessagesOnce();

            // Home view
            Developer.DebugMessage("hello-bar.fxml", true);

            FXMLLoader homeViewLoader = new FXMLLoader(getClass().getResource("hello-view.fxml"));
            Parent homeView = homeViewLoader.load();
            HomeController homeController = homeViewLoader.getController();
            container.Register(HomeController.class, homeController);

            Developer.DebugMessage("Loading home-view.fxml: " + (homeView != null ? "success" : "failed"));
            Developer.DeindentDebugMessagesOnce();

            BorderPane borderPane = new BorderPane();
            borderPane.setCenter(homeView);
            borderPane.setBottom(statusBarView);

            root.getChildren().add(borderPane);

            return new Scene(root);
        } catch (IOException e){
            throw new RuntimeException("Failed to build scene: " + e.getMessage(), e);
        }
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