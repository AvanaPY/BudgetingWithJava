package org.sture.java.budgeting;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseButton;
import org.sture.java.budgeting.controller.settings.CategoryCell;
import org.sture.java.budgeting.services.BudgetTypeCategoryProvider;
import org.sture.java.budgeting.services.tracking.models.BudgetEntryCategory;
import org.sture.java.budgeting.services.tracking.models.BudgetEntrySubCategory;

import java.util.Arrays;

public class SettingsController {
    @FXML ListView<BudgetEntryCategory> categoriesListView;
    @FXML ListView<BudgetEntrySubCategory> subCategoriesListView;

    private final ObservableList<BudgetEntryCategory> categoriesListViewList = FXCollections.observableArrayList();
    private final ObservableList<BudgetEntrySubCategory> subCategoriesListViewList = FXCollections.observableArrayList();

    private BudgetTypeCategoryProvider budgetTypeCategoryProvider;

    public void initialize(){
        budgetTypeCategoryProvider = BudgetApplication.getContainer().ResolveInstance(BudgetTypeCategoryProvider.class);

        categoriesListView.setCellFactory(budgetEntryCategoryListView -> new CategoryCell());
        categoriesListView.setOnMouseClicked(mouseEvent -> {
            if(mouseEvent.getButton() == MouseButton.PRIMARY
                    && mouseEvent.getClickCount() == 2
                    && categoriesListView.getSelectionModel().getSelectedItem() != null){
                OnSelectedCategoryDoubleClicked();
            }
        });

        categoriesListView.setItems(categoriesListViewList);
        categoriesListViewList.addAll(Arrays.asList(budgetTypeCategoryProvider.GenerateAllBudgetCategories()));

        categoriesListView.getSelectionModel().selectIndices(0);
        subCategoriesListView.setItems(subCategoriesListViewList);
        subCategoriesListViewList.setAll(
                Arrays.asList(budgetTypeCategoryProvider.GenerateSubCategoriesFromBudgetCategory(
                        categoriesListView.getSelectionModel().getSelectedItem()
                )));
    }

    public void OnSelectedCategoryDoubleClicked(){
        var selected = categoriesListView.getSelectionModel().getSelectedItem();
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
        subCategoriesListViewList.addAll(
                Arrays.asList(budgetTypeCategoryProvider.GenerateSubCategoriesFromBudgetCategory(
                        cat
                )));
    }
}
