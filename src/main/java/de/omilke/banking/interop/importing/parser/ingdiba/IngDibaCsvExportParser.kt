package de.omilke.banking.interop.importing.parser.ingdiba

import de.omilke.banking.account.entity.Entry
import de.omilke.banking.interop.importing.numbers.LocaleBigDecimalParser
import de.omilke.banking.interop.importing.parser.EntryParser
import org.apache.logging.log4j.LogManager
import java.math.BigDecimal
import java.text.ParseException
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class IngDibaCsvExportParser : EntryParser {

    override fun extractEntry(line: String?): Optional<Entry> {

        LOGGER.trace("Parsing line: {}", line)

        when {
            line == null || line.isEmpty() -> return skipInvalidLine(line)
            else -> {

                val split = line.split(';')
                when {
                    split.size != 9 -> return skipInvalidLine(line)
                    else -> {

                        val entryDate = findEntryDate(split[0])
                        val amount = findAmount(split[7])
                        val comment = findComment(split[3], split[2], split[4])

                        val entry = Entry(amount, entryDate, comment, "imported")

                        return Optional.of(entry)
                    }
                }
            }
        }
    }

    private fun findComment(bookingType: String, receiver: String, text: String): String {

        val trimmedReceiver = trimQuotesAndWhitespaces(receiver)
        val trimmedText = trimQuotesAndWhitespaces(text)

        return if (stringsAreEmpty(trimmedReceiver, trimmedText)) {
            bookingType
        } else {
            "$trimmedReceiver - $trimmedText"
        }
    }

    private fun stringsAreEmpty(vararg strings: String): Boolean {

        for (string in strings) {
            if (string.isEmpty().not()) {
                return false
            }
        }

        return true
    }

    private fun trimQuotesAndWhitespaces(receiver: String): String {

        return receiver.replace("\"", "").trim { it <= ' ' }
    }

    private fun findEntryDate(text: String): LocalDate {

        return LocalDate.parse(text, DTF)
    }

    private fun findAmount(relevantPart: String): BigDecimal {

        val bdParser = LocaleBigDecimalParser()

        try {
            return bdParser.parseBigDecimal(relevantPart)
        } catch (e: ParseException) {
            return BigDecimal.ZERO
        }


    }

    companion object {

        private val LOGGER = LogManager.getLogger(IngDibaCsvExportParser::class.java)

        private val DTF = DateTimeFormatter.ofPattern("dd.MM.yyyy")
    }
}
