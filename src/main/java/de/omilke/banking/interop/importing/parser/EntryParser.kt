package de.omilke.banking.interop.importing.parser

import de.omilke.banking.account.entity.Entry
import org.apache.logging.log4j.Level
import org.apache.logging.log4j.LogManager
import java.util.*

interface EntryParser {

    /**
     * Returns the information parsed from the line.
     *
     * @param line The line containing entry information
     * @return Returns the parsed Entry for the provided line. May be [Optional.empty] if the
     * line is somehow illegal.
     */
    fun extractEntry(line: String): Optional<Entry>

    /**
     * Logs a message and returns `[Optional.empty]` according the contract of [.extractEntry].
     *
     * @param line The invalid line.
     * @return `[Optional.empty]` because this line is invalid.
     */
    fun skipInvalidLine(line: String): Optional<Entry> {

        val logger = LogManager.getLogger(this.javaClass)
        logger.log(Level.WARN, "Line could not be parsed successfully: {}", line)

        return Optional.empty()
    }
}