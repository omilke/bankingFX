package de.omilke.banking.interop.importing.parser.jhaushalt

import de.omilke.banking.account.entity.Entry
import de.omilke.banking.interop.importing.io.LinesReader
import de.omilke.banking.interop.importing.numbers.LocaleBigDecimalParser
import de.omilke.banking.interop.importing.parser.EntryParser
import org.apache.commons.lang3.StringUtils
import org.apache.logging.log4j.Level
import org.apache.logging.log4j.LogManager
import java.text.ParseException
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.regex.Pattern

class JHaushaltParser : EntryParser {

    private val orderRewriter: OrderRewriter
    private val subcategoryRewriter: SubcategoryRewriter

    constructor(orderFilePath: String?, subCategoriesFilePath: String?) {

        LOGGER.log(Level.INFO, "Loading order rewrite rules from: {}", orderFilePath)
        orderRewriter = OrderRewriter(LinesReader.readLinesFromPath(orderFilePath))
        LOGGER.log(Level.INFO, "Loaded order rewrite rules: {}", orderRewriter.mappingCount)

        LOGGER.log(Level.INFO, "Loading sub-category rewrite rules from: {}", subCategoriesFilePath)
        subcategoryRewriter = SubcategoryRewriter(LinesReader.readLinesFromPath(subCategoriesFilePath))
        LOGGER.log(Level.INFO, "Loaded Sub-Category rewrite rules: {} ", subcategoryRewriter.mappingCount)
    }


    override fun extractEntry(line: String): Optional<Entry> {

        LOGGER.trace("Parsing line: {}", line)

        return when {
            isLineValid(line).not() -> skipInvalidLine(line)
            else -> {
                try {
                    parseLine(line)
                } catch (e: ParseException) {
                    skipInvalidLine(line)
                }
            }
        }
    }

    /**
     * Checks whether the line is fit for parsing.
     *
     * @param line The line to be checked.
     * @return `true` if the line can be parsed, `false` otherwise.
     */
    private fun isLineValid(line: String): Boolean {

        // count the quotes to verify removing all is ok
        val count = StringUtils.countMatches(line, "\"")

        return count == 10
    }

    private fun parseLine(line: String): Optional<Entry> {
        val scanner = setupScannerForLine(line)

        val date = scanner.next()

        val entryDate = LocalDate.from(DTF.parse(date))

        val comment = scanner.next().trim { it <= ' ' }

        var category = scanner.next()

        if (category.startsWith("[[") && category.endsWith("]]")) {
            return Optional.empty()
        }

        val amountString = scanner.next()
        val trimmedAmount = StringUtils.substringBefore(amountString, " ")

        val amount = LocaleBigDecimalParser(Locale.GERMANY).parseBigDecimal(trimmedAmount)

        val isSaving = this.checkForSaving(comment, category)

        // technically create a new sub-category from this subset of entries for later analyses
        category = subcategoryRewriter.map(category, comment)

        val (sequence, orderIndex) = this.orderRewriter.map(comment)

        val parsedEntry = Entry(amount, entryDate, comment, category, isSaving, sequence, orderIndex)

        return Optional.of(parsedEntry)
    }

    private fun setupScannerForLine(line: String): Scanner {

        val preparedLine = StringUtils.remove(line, '"')

        val scanner = Scanner(preparedLine)
        scanner.useDelimiter(Pattern.compile(";"))

        return scanner
    }

    private fun checkForSaving(comment: String, category: String): Boolean {

        return when {
            //exception for special entry
            //TODO: better fix this is data, not in import
            category.equals("dk 2010", true) -> false
            //check comment
            StringUtils.containsIgnoreCase(comment, "Entnahme") -> true
            StringUtils.containsIgnoreCase(comment, "Rücklage") -> true
            // check category
            StringUtils.containsIgnoreCase(category, "Verliehen") -> true
            StringUtils.containsIgnoreCase(category, "Rücklage") -> true
            else -> false
        }
    }

    companion object {

        private val LOGGER = LogManager.getLogger(JHaushaltParser::class.java)

        private val DTF = DateTimeFormatter.ofPattern("dd.MM.yy")
    }

}
