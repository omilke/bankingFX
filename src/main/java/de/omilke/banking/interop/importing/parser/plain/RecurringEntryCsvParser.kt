package de.omilke.banking.interop.importing.parser.plain

import de.omilke.banking.account.entity.*
import de.omilke.banking.interop.importing.parser.RecurringEntryParser
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class RecurringEntryCsvParser : RecurringEntryParser {

    override fun extractEntry(line: String?): Optional<RecurringEntry> {

        LOGGER.trace("Parsing line: {}", line)

        when {
            line == null || line.isEmpty() -> return skipInvalidLine(line)

            else -> {

                val split = line.split(';')
                return when {
                    split.size != 8 -> skipInvalidLine(line)
                    else -> {

                        try {

                            val amount = split[0].toBigDecimal()
                            val startOfRecurrence: LocalDate = LocalDate.from(DTF.parse(split[1]))
                            val comment = split[2]
                            val category = split[3]
                            val isSaving = split[4].toBoolean()
                            val sequence = EntrySequence.valueOf(split[5])
                            val orderIndex = split[6].toInt()
                            val recurrenceStrategy = findStrategy(split[7])

                            if (recurrenceStrategy == null) {
                                return skipInvalidLine(line)
                            } else {
                                val entry = RecurringEntry(amount, startOfRecurrence, null, comment, category, isSaving, sequence, orderIndex, recurrenceStrategy)
                                Optional.of(entry)
                            }
                        } catch (e: Exception) {
                            return skipInvalidLine(line, e)
                        }
                    }
                }
            }
        }
    }

    private fun findStrategy(s: String): RecurrenceStrategy? {

        return when (s) {
            StartOfMonthRecurrence.javaClass.simpleName -> StartOfMonthRecurrence
            EndOfMonthRecurrence.javaClass.simpleName -> EndOfMonthRecurrence
            else -> null
        }
    }

    companion object {

        val LOGGER: Logger = LogManager.getLogger(RecurringEntryParser::javaClass)

        private val DTF = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    }
}