package org.sture.java.budgeting.store.category.service;

import org.sture.java.budgeting.services.job.BackgroundJobExecutionService;
import org.sture.java.budgeting.services.tracking.models.BudgetEntryCategory;
import org.sture.java.budgeting.store.StoreService;
import org.sture.java.budgeting.store.category.dto.CategoryDTO;
import org.sture.java.budgeting.store.category.dto.CategoryDTOConverter;

public class CategoryStoreService extends StoreService<BudgetEntryCategory, CategoryDTO> {
    public CategoryStoreService(
            CategoryDTOConverter dtoFactory,
            BackgroundJobExecutionService jobExecutionerService) {
        super("budgetCategory.store", dtoFactory, jobExecutionerService);
    }
}
