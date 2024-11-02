package org.sture.java.budgeting.store.budgetcategory.service;

import org.sture.java.budgeting.services.job.BackgroundJobExecutionService;
import org.sture.java.budgeting.services.tracking.models.BudgetEntryCategory;
import org.sture.java.budgeting.store.StoreService;
import org.sture.java.budgeting.store.budgetcategory.dto.BudgetEntryCategoryDTO;
import org.sture.java.budgeting.store.budgetcategory.dto.BudgetEntryCategoryDTOConverter;

public class BudgetCategoryStoreService extends StoreService<BudgetEntryCategory, BudgetEntryCategoryDTO> {
    public BudgetCategoryStoreService(
            BudgetEntryCategoryDTOConverter dtoFactory,
            BackgroundJobExecutionService jobExecutionerService) {
        super("budgetCategory.store", dtoFactory, jobExecutionerService);
    }
}
