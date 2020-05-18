package de.omilke.bankingfx.report.categories.service

import de.omilke.banking.persistence.PersistenceServiceProvider.persistenceService
import de.omilke.bankingfx.report.categories.model.CategoryReportModel
import javafx.concurrent.Task
import java.time.LocalDate

/**
 * Created by Olli on 04.06.2017.
 */
class FetchEntriesAndBuildModelTask(private val from: LocalDate?, private val to: LocalDate?, private val selectedCategory: String?) : Task<CategoryReportModel>() {

    override fun call(): CategoryReportModel {

        val resultList = persistenceService.entryRepository
                .findAllEntriesBetweenWithCategoryName(from, to, selectedCategory)

        return CategoryReportModel(resultList)
    }

    override fun succeeded() {}

}