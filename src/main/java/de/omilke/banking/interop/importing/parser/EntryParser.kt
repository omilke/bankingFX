package de.omilke.banking.interop.importing.parser;

import de.omilke.banking.account.entity.Entry;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

public interface EntryParser {

    /**
     * Returns the information parsed from the line.
     *
     * @param line The line containing entry information
     * @return Returns the parsed Entry for the provided line. May be {@link Optional#empty()} if the
     * line is somehow illegal.
     */
    Optional<Entry> extractEntry(String line);

    /**
     * Logs a message and returns <code>{@link Optional#empty()}</code> according the contract of {@link #extractEntry(String)}.
     *
     * @param line The invalid line.
     * @return <code>{@link Optional#empty()}</code> because this line is invalid.
     */
    default Optional<Entry> skipInvalidLine(String line) {

        Logger logger = LogManager.getLogger(this.getClass());
        logger.log(Level.WARN, "Line could not be parsed successfully: {}", line);

        return Optional.empty();
    }

}
