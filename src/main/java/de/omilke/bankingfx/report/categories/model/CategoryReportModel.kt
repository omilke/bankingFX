package de.omilke.bankingfx.report.categories.model

import de.omilke.banking.BankingConfigurator
import de.omilke.banking.account.entity.Entry
import de.omilke.util.DurationProvider
import javafx.collections.FXCollections
import org.apache.logging.log4j.Level
import org.apache.logging.log4j.LogManager
import java.math.BigDecimal
import java.text.Collator
import java.time.YearMonth
import java.util.*

/**
 * Created by Olli on 25.05.2017.
 */
class CategoryReportModel(entries: List<Entry>) {

    private val comparator = Collator.getInstance(BankingConfigurator.configuredLocale())

    private val categoriesOverTimeMatrix: MutableMap<CategoryKey, BigDecimal?> = HashMap()

    val yearMonthColumns: SortedSet<YearMonth> = TreeSet()
    val categoriesWithExpenditure: SortedSet<String>

    private val model: MutableList<CategoryOverTimeElement> = ArrayList()

    /**
     * Produces the data points for the categories view.
     *
     *
     * The values of [CategoryOverTimeElement.getMonthValues] are sorted with ascending order.
     * The CategoryModelElements will be in ascending order of their category names.
     */
    fun getModelData(): List<CategoryOverTimeElement> = model

    /**
     * Creates the model for displaying in the view. This is a slow operation.
     */
    init {

        val nanoTime = System.nanoTime()

        categoriesWithExpenditure = TreeSet(comparator)
        for ((amount, entryDate, _, category) in entries) {
            val key = CategoryKey.of(entryDate, category)

            yearMonthColumns += key.yearMonth
            categoriesWithExpenditure += key.category

            // start with zero for a new month and add all entries in the same month
            val sumSoFar = categoriesOverTimeMatrix.getOrDefault(key, BigDecimal.ZERO)
            categoriesOverTimeMatrix[key] = sumSoFar!!.add(amount)
        }

        for (category in categoriesWithExpenditure) {

            val expenditureSums = FXCollections.observableArrayList<BigDecimal?>()
            for (yearMonth in yearMonthColumns) {
                val key = CategoryKey(yearMonth, category)
                expenditureSums += categoriesOverTimeMatrix.getOrDefault(key, null)
            }

            model.add(CategoryOverTimeElement(category, expenditureSums))
        }

        //add additional sum model element as last row
        model.add(CategoryOverTimeElement.buildSumOverTimeElement(model))

        LOGGER.log(Level.INFO, "Building report model took {}", DurationProvider.formatDurationSince(nanoTime))
    }

    companion object {
        private val LOGGER = LogManager.getLogger(CategoryReportModel::class.java)
    }

}