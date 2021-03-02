package de.omilke.banking.interop.exporting.csv

import de.omilke.banking.account.entity.Entry
import de.omilke.banking.interop.exporting.EntryFormatter
import java.math.BigDecimal
import java.text.DecimalFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class CsvFormatter : EntryFormatter {

    override fun format(entry: Entry): String {

        return "${formatDate(entry.entryDate)}; ${entry.category}; ${entry.comment}; ${formatAmount(entry.amount)}"
    }

    private fun formatAmount(amount: BigDecimal): String {

        return DecimalFormat("0.00").format(amount)
    }

    private fun formatDate(entryDate: LocalDate): String {

        return entryDate.format(DTF)
    }

    companion object {

        private val DTF = DateTimeFormatter.ofPattern("dd.MM.yy")
    }
}