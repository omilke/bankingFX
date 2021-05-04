package de.omilke.bankingfx.report.savings

import de.omilke.banking.persistence.PersistenceServiceProvider.persistenceService
import de.omilke.bankingfx.report.savings.model.Category
import de.omilke.bankingfx.report.savings.model.Entry
import de.saxsys.mvvmfx.ViewModel
import java.util.*

class SavingsModel : ViewModel {

    private val er = persistenceService.entryRepository

    val categories: MutableList<Category> = ArrayList()

    fun initialize() {

        prepareModel()
    }

    private fun prepareModel() {

        for (current in er.findAllEntries()) {
            if (current.isSaving) {
                val category = findCategoryByName(current.category)
                category.addEntry(Entry(current))
            }
        }

        categories.sort()
    }

    private fun findCategoryByName(name: String): Category {

        for (category in categories) {

            if (category.name.equals(name, ignoreCase = true)) {
                return category
            }
        }

        val newCategory = Category(name)
        categories.add(newCategory)

        return newCategory
    }
}