package de.omilke.banking.persistence.jooq

import de.omilke.util.DurationProvider
import org.apache.logging.log4j.Level
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import java.math.BigDecimal
import java.time.LocalDate
import java.time.Period
import java.time.YearMonth
import java.util.*

internal class ReportServiceJooq {

    private val queryExecutor = JooqQueryExecutor()

    fun getCategorySums(start: LocalDate?, end: LocalDate?) {

        val nanoTime = System.nanoTime()
        val sumsByMonthAndCategory = queryExecutor.getSumsByMonthAndCategory(start, end)

        LOGGER.log(Level.INFO, "fetching ${sumsByMonthAndCategory?.size} category sums took {}", DurationProvider.formatDurationSince(nanoTime))

        val reportModelBuilder = CategoryReportModelBuilder()

        val nanoTime1 = System.nanoTime()
        for (currentRecord in sumsByMonthAndCategory!!) {
            reportModelBuilder.putRecord(
                    currentRecord.component1(),
                    currentRecord.component2(),
                    currentRecord.component3(),
                    currentRecord.component4()
            )
        }

        reportModelBuilder.getPeriod()

        LOGGER.log(Level.INFO, "iterating ${sumsByMonthAndCategory.size} category sums took {}", DurationProvider.formatDurationSince(nanoTime))
    }

    companion object {

        val LOGGER: Logger = LogManager.getLogger(ReportServiceJooq::class.java)
    }
}

private class CategoryReportModelBuilder {

    private val categoryPeriodSums: MutableMap<Pair<String?, YearMonth?>, BigDecimal> = HashMap()

    var min: YearMonth = YearMonth.of(9999, 12)
    var max: YearMonth = YearMonth.of(1, 1)

    fun putRecord(categoryName: String?, year: Int?, month: Int?, amount: BigDecimal) {

        if (year != null && month != null) {
            val currentPeriod = YearMonth.of(year, month)

            if (currentPeriod.isBefore(min)) min = currentPeriod
            if (currentPeriod.isAfter(max)) max = currentPeriod

            categoryPeriodSums[Pair(categoryName, currentPeriod)] = amount
        } else {
            categoryPeriodSums[Pair(categoryName, null)] = amount
        }
    }

    fun getPeriod(): Period {
        return Period.between(min.atDay(1), max.plusMonths(1).atDay(1))
    }

}
