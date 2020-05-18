package de.omilke.bankingfx.report.fortunehistory

import de.omilke.banking.persistence.PersistenceServiceProvider.persistenceService
import de.omilke.bankingfx.UIConstants
import de.omilke.bankingfx.controls.UIUtils
import de.omilke.bankingfx.controls.extensions.asYearMonth
import de.omilke.bankingfx.report.control.setChartValues
import de.omilke.bankingfx.report.control.setContextMenu
import de.omilke.bankingfx.report.control.setToolTips
import de.omilke.bankingfx.report.control.styleAxes
import de.saxsys.mvvmfx.FxmlView
import javafx.fxml.FXML
import javafx.scene.chart.AreaChart
import java.math.BigDecimal
import java.time.LocalDate
import java.time.YearMonth
import java.util.*

class FortunehistoryView : FxmlView<FortunehistoryModel> {

    private val er = persistenceService.entryRepository

    @FXML
    lateinit var fortuneChart: AreaChart<String, BigDecimal>

    fun initialize() {


        setupChart()
    }

    private fun setupChart() {

        fortuneChart.setChartValues(getFortuneByMonth(), ::formatEntryDate)

        fortuneChart.styleAxes()
        fortuneChart.setToolTips(::buildTooltipText)
        fortuneChart.setContextMenu(::parseEntryDate)
    }

    private fun formatEntryDate(date: YearMonth): String {

        return date.plusMonths(1).atDay(1).format(UIConstants.DATE_FORMATTER)
    }

    private fun parseEntryDate(text: String): YearMonth {

        val parsedDate = LocalDate.parse(text, UIConstants.DATE_FORMATTER)
        return YearMonth.of(parsedDate.year, parsedDate.month).minusMonths(1)
    }

    private fun buildTooltipText(category: String, value: BigDecimal): String {

        return "${UIUtils.formatAmount(value)} [${category}]"
    }

    /**
     * Produces the data points for the fortune chart.
     */
    private fun getFortuneByMonth(): Map<YearMonth, BigDecimal> {

        val entries = er.findAllEntries()
        entries.sortBy { it.entryDate }

        val fortuneByMonth = LinkedHashMap<YearMonth, BigDecimal>()

        var runningTotal = BigDecimal.ZERO
        for ((amount, entryDate) in entries) {
            val category = entryDate.asYearMonth()

            /**
             * Sum up all entries and then set the total sum to the map. Once a new month is reached, the old map entry is not touched and
             * therefore contains the sum of all entries up the last entry. Requires chronologically ascending order of the entries!
             */
            runningTotal += amount
            fortuneByMonth[category] = runningTotal
        }

        return fortuneByMonth
    }
}