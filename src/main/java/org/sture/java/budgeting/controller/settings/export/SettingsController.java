package org.sture.java.budgeting.controller.settings.export;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseButton;
import org.sture.java.budgeting.controller.iface.BudgetController;
import org.sture.java.budgeting.controller.settings.CategoryCell;
import org.sture.java.budgeting.di.DiContainer;
import org.sture.java.budgeting.services.category.CategoryService;
import org.sture.java.budgeting.services.tracking.models.BudgetEntryCategory;

import java.util.Arrays;

public class SettingsController implements BudgetController {
    @FXML ListView<BudgetEntryCategory> categoriesListView;
    @FXML ListView<BudgetEntryCategory> subCategoriesListView;

    private final ObservableList<BudgetEntryCategory> categoriesListViewList = FXCollections.observableArrayList();
    private final ObservableList<BudgetEntryCategory> subCategoriesListViewList = FXCollections.observableArrayList();

    private CategoryService categoryService;

    @Override
    public void InitializeControllerWithContainer(DiContainer container) {
        categoryService = container.ResolveInstance(CategoryService.class);

        categoriesListView.setCellFactory(budgetEntryCategoryListView -> new CategoryCell());
        categoriesListView.setOnMouseClicked(mouseEvent -> {
            if(mouseEvent.getButton() == MouseButton.PRIMARY ) {
                categoriesListViewClicked();
            }
        });

        categoriesListView.setItems(categoriesListViewList);
        categoriesListViewList.addAll(Arrays.asList(categoryService.GenerateAllBudgetCategories()));

        categoriesListView.getSelectionModel().selectIndices(0);
        subCategoriesListView.setItems(subCategoriesListViewList);
        subCategoriesListViewList.setAll(
                Arrays.asList(categoriesListView
                        .getSelectionModel()
                        .getSelectedItem()
                        .GetSubCategories()
                ));
    }

    public void categoriesListViewClicked(){
        var selectedCategory = categoriesListView.getSelectionModel().getSelectedItem();
        if(selectedCategory != null){
            updateSubCategoriesListViewWithCorrectSubCategoriesForCategory(selectedCategory);
        }
    }

    public void buttonAddCategory(){

    }

    public void buttonAddSubCategory(){

    }

    private void updateSubCategoriesListViewWithCorrectSubCategoriesForCategory(BudgetEntryCategory cat){
        subCategoriesListViewList.clear();
        subCategoriesListViewList.addAll(Arrays.asList(cat.GetSubCategories()));
    }
}
