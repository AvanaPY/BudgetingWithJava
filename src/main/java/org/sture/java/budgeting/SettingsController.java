package org.sture.java.budgeting;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseButton;
import org.sture.java.budgeting.controller.settings.CategoryCell;
import org.sture.java.budgeting.services.BudgetTypeCategoryProvider;
import org.sture.java.budgeting.services.tracking.models.BudgetEntryCategory;

import java.util.Arrays;

public class SettingsController {
    @FXML ListView<BudgetEntryCategory> categoriesListView;
    @FXML ListView<BudgetEntryCategory> subCategoriesListView;

    private final ObservableList<BudgetEntryCategory> categoriesListViewList = FXCollections.observableArrayList();
    private final ObservableList<BudgetEntryCategory> subCategoriesListViewList = FXCollections.observableArrayList();

    private BudgetTypeCategoryProvider budgetTypeCategoryProvider;

    public void initialize(){
        budgetTypeCategoryProvider = BudgetApplication.getContainer().ResolveInstance(BudgetTypeCategoryProvider.class);

        categoriesListView.setCellFactory(budgetEntryCategoryListView -> new CategoryCell());
        categoriesListView.setOnMouseClicked(mouseEvent -> {
            if(mouseEvent.getButton() == MouseButton.PRIMARY ) {
                categoriesListViewClicked();
            }
        });

        categoriesListView.setItems(categoriesListViewList);
        categoriesListViewList.addAll(Arrays.asList(budgetTypeCategoryProvider.GenerateAllBudgetCategories()));

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
