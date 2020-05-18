package de.omilke.banking.interop.exporting.jhaushalt

import de.omilke.banking.account.entity.Entry
import de.omilke.banking.interop.exporting.EntryFormatter
import java.math.BigDecimal
import java.text.DecimalFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * Created by Olli on 13.04.2017.
 */
class JHaushaltFormatter @JvmOverloads constructor(private val applyActualCategory: Boolean = false) : EntryFormatter {

    override fun format(entry: Entry): String {
        return format(entry.entryDate, entry.amount, entry.comment, entry.category)
    }

    fun format(entryDate: LocalDate, amount: BigDecimal, comment: String, categoryName: String): String {

        val relevantCategoryName: String = when {
            applyActualCategory -> categoryName
            else -> DEFAULT_CATEGORY
        }

        return String.format(PATTERN, formatDate(entryDate), comment, relevantCategoryName, formatAmount(amount))
    }

    private fun formatAmount(amount: BigDecimal): String {

        return DecimalFormat("0.00").format(amount)
    }

    private fun formatDate(entryDate: LocalDate): String {

        return entryDate.format(DTF)
    }

    companion object {

        private const val PATTERN = """"%s";"%s";"%s";"%s ¬";"[Giro]""""
        private const val DEFAULT_CATEGORY = "exported"

        private val DTF = DateTimeFormatter.ofPattern("dd.MM.yy")
    }

}