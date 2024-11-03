package org.sture.java.budgeting.controller.settings.export;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import org.sture.java.budgeting.controller.iface.BudgetController;
import org.sture.java.budgeting.controller.settings.CategoryCell;
import org.sture.java.budgeting.di.DiContainer;
import org.sture.java.budgeting.services.category.CategoryService;
import org.sture.java.budgeting.services.tracking.models.BudgetEntryCategory;

import java.util.Arrays;

public class SettingsController implements BudgetController {
    @FXML ListView<BudgetEntryCategory> categoriesListView;
    @FXML ListView<BudgetEntryCategory> subCategoriesListView;

    @FXML TextField categoryNameTextField;
    @FXML TextField subCategoryNameTextField;

    private final ObservableList<BudgetEntryCategory> categoriesListViewList = FXCollections.observableArrayList();
    private final ObservableList<BudgetEntryCategory> subCategoriesListViewList = FXCollections.observableArrayList();

    private CategoryService categoryService;

    @Override
    public void InitializeControllerWithContainer(DiContainer container) {
        categoryService = container.ResolveInstance(CategoryService.class);

        categoriesListView.setCellFactory(BudgetEntryCategory -> new CategoryCell());

        categoriesListView.setItems(categoriesListViewList);
        categoriesListViewList.addAll(Arrays.asList(categoryService.GenerateAllBudgetCategories()));
        subCategoriesListView.setItems(subCategoriesListViewList);

        selectCategory(0);
        refreshCategoriesAndSubCategoriesLists();
    }

    public void categoriesListViewClicked(){
        var selectedCategory = getSelectedCategory();
        if(selectedCategory != null){
            updateSubCategoriesListViewWithCorrectSubCategoriesForCategory(selectedCategory);
            updateCategoryNameTextFieldWithSelectedCategoryInformation();
        }
    }

    // ----------------All of our buttons----------------

    public void buttonAddCategory(){
        System.out.println("Adding category!");

        String newCategoryName = getCategoryTextName();
        if(newCategoryName == null){
            return;
        }

        BudgetEntryCategory existingCategory = findCategoryByName(newCategoryName);
        if(existingCategory != null){
            selectCategory(existingCategory);
            return;
        }

        var newCategory = new BudgetEntryCategory(newCategoryName, false);
        addNewCategory(newCategory);
        clearAllSelection();
        selectCategory(newCategory);
    }

    public void buttonUpdateCategory(){
        System.out.println("Updating category!");

        String newCategoryName = getCategoryTextName();
        var selectedCategory = getSelectedCategory();
        var existingCategory = findCategoryByName(newCategoryName);
        if(newCategoryName == null || selectedCategory == null || existingCategory != null){
            return;
        }

        categoryService.updateCategory(
                selectedCategory,
                newCategoryName,
                selectedCategory.isPositive(),
                selectedCategory.GetSubCategories());
        refreshCategoriesAndSubCategoriesLists();
    }

    public void buttonDeleteCategory(){
        System.out.println("Deleting category!");

        var selectedCategory = getSelectedCategory();
        var selectedIndex = getSelectedCategoryIndex();
        if(selectedCategory != null){
            categoryService.deleteCategoryByName(selectedCategory.name());
            refreshCategoriesAndSubCategoriesLists();
            if(categoriesListView.getItems().size() > selectedIndex){
                selectCategory(selectedIndex);
            } else {
                clearAllSelection();
            }
        }
    }

    public void buttonAddSubCategory(){
        System.out.println("Adding sub category!");

        String subCategoryName = getSubCategoryTextName();
        if(subCategoryName == null)
            return;

        var selectedCategory = getSelectedCategory();
        if(selectedCategory == null)
            return;

        var existingSubCategory = findSubCategoryByName(selectedCategory.name(), subCategoryName);
        if(existingSubCategory != null){
            selectSubCategory(existingSubCategory);
            return;
        }

        var newSubCategory = new BudgetEntryCategory(subCategoryName, false);
        addNewSubCategory(selectedCategory.name(), newSubCategory);

        refreshCategoriesAndSubCategoriesLists();

        clearAllSelection();
        selectCategory(selectedCategory);
        selectSubCategory(newSubCategory);
    }

    public void buttonUpdateSubCategory(){
        System.out.println("Updating sub category!");

        String newName = getSubCategoryTextName();
        var selectedSubCategory = getSelectedSubCategory();
        if(newName == null || selectedSubCategory == null)
            return;

        var existingsubCategory = findSubCategoryByName(selectedSubCategory.name(), newName);
        if(existingsubCategory != null)
            return;

        categoryService.updateSubCategory(
                selectedSubCategory,
                newName,
                selectedSubCategory.isPositive());
        refreshCategoriesAndSubCategoriesLists();
    }

    public void buttonDeleteSubCategory(){
        System.out.println("Deleting sub category!");

        var selectedCategory = getSelectedCategory();
        var selectedSubCategory = getSelectedSubCategory();
        var index = getSelectedSubCategoryIndex();
        if(selectedCategory == null || selectedSubCategory == null)
            return;

        categoryService.deleteSubCategoryByName(selectedCategory, selectedSubCategory.name());
        refreshCategoriesAndSubCategoriesLists();
        clearAllSelection();
        selectCategory(selectedCategory);
        selectSubCategory(index);
    }

    //----------------Some wrappers for more readable code----------------

    private BudgetEntryCategory getSelectedCategory(){
        return categoriesListView.getSelectionModel().getSelectedItem();
    }

    private int getSelectedCategoryIndex(){
        return categoriesListView.getSelectionModel().getSelectedIndex();
    }

    private void selectCategory(BudgetEntryCategory category){
        categoriesListView.getSelectionModel().select(category);
        categoryNameTextField.setText(categoriesListView.getSelectionModel().getSelectedItem().name());
    }

    private void selectCategory(int index){
        if(index >= 0 && index < categoriesListViewList.size()){
            categoriesListView.getSelectionModel().select(index);
            categoryNameTextField.setText(categoriesListView.getSelectionModel().getSelectedItem().name());
        }
    }

    private BudgetEntryCategory getSelectedSubCategory(){
        return subCategoriesListView.getSelectionModel().getSelectedItem();
    }

    private int getSelectedSubCategoryIndex(){
        return subCategoriesListView.getSelectionModel().getSelectedIndex();
    }

    private void selectSubCategory(BudgetEntryCategory subCategory){
        subCategoriesListView.getSelectionModel().select(subCategory);
        subCategoryNameTextField.setText(subCategoriesListView.getSelectionModel().getSelectedItem().name());
    }

    private void selectSubCategory(int index){
        if(index >= 0 && index < subCategoriesListViewList.size()){
            subCategoriesListView.getSelectionModel().select(index);
            System.out.println(subCategoriesListView.getItems());
            System.out.println(index);
            System.out.println(subCategoriesListView.getSelectionModel().getSelectedItem());
            subCategoryNameTextField.setText(subCategoriesListView.getSelectionModel().getSelectedItem().name());
        }
    }

    private void clearAllSelection(){
        categoriesListView.getSelectionModel().clearSelection();
        subCategoriesListView.getSelectionModel().clearSelection();
        categoryNameTextField.clear();
        subCategoryNameTextField.clear();
    }

    /**
     * Gets the current name from the Category Name Text field. If the name is considered invalid, this returns null instead.
     * @return String
     */
    private String getCategoryTextName(){
        String categoryName = categoryNameTextField.getText();
        if(categoryName.length() < 3)
            return null;
        return categoryName;
    }

    /**
     * Gets the current name from the Sub Category Name Text field. If the name is considered invalid, this returns null instead.
     * @return String
     */
    private String getSubCategoryTextName(){
        String subCategoryName = subCategoryNameTextField.getText();
        if(subCategoryName.length() < 3)
            return null;
        return subCategoryName;
    }

    //----------------Some useful private methods and wrappers----------------

    private BudgetEntryCategory findCategoryByName(String name){
        return categoryService.findCategoryByName(name);
    }

    private void addNewCategory(BudgetEntryCategory category){
        categoryService.addNewCategory(category);
        refreshCategoriesAndSubCategoriesLists();
    }

    private BudgetEntryCategory findSubCategoryByName(String categoryName, String subCategoryName){
        return categoryService.findSubCategoryByName(categoryName, subCategoryName);
    }

    private void addNewSubCategory(String categoryName, BudgetEntryCategory subCategory){
        categoryService.addNewSubCategory(categoryName, subCategory);
        refreshCategoriesAndSubCategoriesLists();
    }

    private void refreshCategoriesAndSubCategoriesLists(){
        var selectedCategoryIndex = getSelectedCategoryIndex();
        var selectedSubCategoryIndex = getSelectedSubCategoryIndex();

        categoriesListViewList.clear();
        subCategoriesListViewList.clear();

        categoriesListViewList.addAll(categoryService.GenerateAllBudgetCategories());
        selectCategory(selectedCategoryIndex);

        var selectedCategory = categoriesListView.getSelectionModel().getSelectedItem();
        if(selectedCategory != null) {
            subCategoriesListViewList.addAll(selectedCategory.GetSubCategories());
        }

        selectSubCategory(selectedSubCategoryIndex);
    }

    private void updateSubCategoriesListViewWithCorrectSubCategoriesForCategory(BudgetEntryCategory cat){
        subCategoriesListViewList.clear();
        subCategoriesListViewList.addAll(Arrays.asList(cat.GetSubCategories()));
    }

    private void updateCategoryNameTextFieldWithSelectedCategoryInformation(){
        var selectedCategory = getSelectedCategory();
        if(selectedCategory == null)
            return;
        categoryNameTextField.setText(selectedCategory.name());
    }
}
