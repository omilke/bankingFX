package de.omilke.bankingfx.report.balance

import de.omilke.banking.persistence.PersistenceServiceProvider.persistenceService
import de.omilke.bankingfx.UIConstants
import de.omilke.bankingfx.controls.UIUtils
import de.omilke.bankingfx.controls.extensions.asYearMonth
import de.omilke.bankingfx.controls.extensions.atStartOfMonth
import de.omilke.bankingfx.report.control.*
import de.saxsys.mvvmfx.FxmlView
import javafx.fxml.FXML
import javafx.scene.chart.BarChart
import java.math.BigDecimal
import java.time.LocalDate
import java.time.YearMonth
import java.util.*

class BalanceView : FxmlView<BalanceModel> {

    private val er = persistenceService.entryRepository

    @FXML
    lateinit var balanceChart: BarChart<String, BigDecimal>

    @FXML
    lateinit var fortuneChart: BarChart<String, BigDecimal>

    fun initialize() {

        setupFortuneChart()
        setupBalanceChart()
    }

    private fun setupBalanceChart() {

        balanceChart.setChartValues(getBalanceByMonth(), ::formatBalanceEntryDate)

        balanceChart.styleAxes()

        balanceChart.setToolTips(::buildTooltipText)
        balanceChart.setContextMenu(::parseBalanceEntryDate)
        balanceChart.styleNegativeValuesWith(UIConstants.NEGATIVE)
    }

    private fun setupFortuneChart() {

        fortuneChart.setChartValues(getFortuneByMonth(), ::formatFortuneEntryDate)

        fortuneChart.styleAxes()

        fortuneChart.setToolTips(::buildTooltipText)

    }

    private fun buildTooltipText(category: String, value: BigDecimal): String {

        return UIUtils.formatAmount(value)
    }

    private fun formatFortuneEntryDate(entryDate: YearMonth): String {

        return entryDate.plusMonths(1).atDay(1).format(UIConstants.DATE_FORMATTER)
    }

    private fun formatBalanceEntryDate(entryDate: YearMonth): String {

        return entryDate.format(UIConstants.MONTH_FORMATTER)
    }

    private fun parseBalanceEntryDate(text: String): YearMonth {

        return YearMonth.parse(text, UIConstants.MONTH_FORMATTER)
    }

    /**
     * Produces the data points for the balance chart.
     */
    private fun getBalanceByMonth(): Map<YearMonth, BigDecimal> {

        val entries = er.findAllEntriesBetweenWithCategoryName(FROM_DEFAULT)
        entries.sortBy { it.entryDate }

        val balanceByMonth = LinkedHashMap<YearMonth, BigDecimal>()
        for ((amount, entryDate) in entries) {

            /*
             * Sum up all entries and then set the total sum to the map. Once a new month is reached,
             * the old map entry is not touched and therefore contains the sum of all entries up the last entry.
             * Requires chronologically ascending order of the entries!
             */

            val category = entryDate.asYearMonth()
            // start with zero for a new month and add all entries in the same month
            val value = balanceByMonth.getOrDefault(category, BigDecimal.ZERO)
            balanceByMonth[category] = value + amount
        }

        return balanceByMonth
    }

    /**
     * Produces the data points for the fortune chart.
     */
    private fun getFortuneByMonth(): Map<YearMonth, BigDecimal> {

        val entries = er.findAllEntries()
        entries.sortBy { it.entryDate }

        /*
         * determine the fortune (sum of all entries) before the relevant period that is being displayed and to allow
         *  building fortune for each displayed month (group sum by month).
         */

        val fortuneByMonth = LinkedHashMap<YearMonth, BigDecimal>()

        var initialSum = BigDecimal.ZERO
        var runningTotal = BigDecimal.ZERO
        for (currentEntry in entries) {

            if (currentEntry.entryDate < FROM_DEFAULT) {

                //add up the initial sum (i. e. sum of entries before the relevant period)
                initialSum += currentEntry.amount
                runningTotal = initialSum
            } else {
                val category = currentEntry.entryDate.asYearMonth()

                //once the loop reaches entries within the relevant period, add up the running total
                //which start off as the initial sum and continues add up over time
                runningTotal += currentEntry.amount
                fortuneByMonth[category] = runningTotal
            }
        }

        return fortuneByMonth
    }

    companion object {
        private const val INCLUDED_PAST_MONTH = 12L

        private val FROM_DEFAULT = LocalDate.now().minusMonths(INCLUDED_PAST_MONTH).atStartOfMonth()
    }
}