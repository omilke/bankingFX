package de.omilke.bankingfx.report.categories.model

import java.time.LocalDate
import java.time.YearMonth

/**
 * Created by Olli on 24.05.2017.
 */
data class CategoryKey(val yearMonth: YearMonth, val category: String) {

    companion object {
        fun of(date: LocalDate, name: String): CategoryKey {
            val month = YearMonth.of(date.year, date.month)
            return CategoryKey(month, name)
        }
    }

}