package de.omilke.banking.account.report

import java.math.BigDecimal
import java.time.YearMonth
import java.util.*

class CategoryReportModel(
        val categoryName: String,
        val periodSums: SortedMap<YearMonth, BigDecimal>,
        val categorySum: BigDecimal,
        val categoryAverage: BigDecimal
)