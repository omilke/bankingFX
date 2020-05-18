package de.omilke.banking.interop.importing.parser

import de.omilke.banking.account.entity.RecurringEntry
import org.apache.logging.log4j.Level
import org.apache.logging.log4j.LogManager
import java.util.*

interface RecurringEntryParser {
    /**
     * Returns the information parsed from the line.
     *
     * @param line The line containing entry information
     * @return Returns the [RecurringEntry] for the provided line. May be empty if the
     * line is somehow illegal.
     */
    fun extractEntry(line: String?): Optional<RecurringEntry>

    /**
     * Logs a message and returns an empty Optional according the contract of [.extractEntry].
     *
     * @param line The invalid line.
     * @return `Optional.empty()` because this line is invalid.
     */
    fun skipInvalidLine(line: String?, e: Exception? = null): Optional<RecurringEntry> {

        LogManager.getLogger(javaClass).log(Level.WARN, "Line could not be parsed successfully: {}", line, e)

        return Optional.empty()
    }
}