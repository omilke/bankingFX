package de.omilke.banking.interop.importing

import de.omilke.banking.account.entity.RecurringEntry
import de.omilke.banking.account.entity.RecurringEntryRepository
import de.omilke.banking.interop.importing.io.LinesReader
import de.omilke.banking.interop.importing.parser.RecurringEntryParser
import de.omilke.banking.interop.importing.parser.plain.RecurringEntryCsvParser
import de.omilke.banking.persistence.PersistenceServiceProvider
import de.omilke.util.DurationProvider
import org.apache.logging.log4j.Level
import org.apache.logging.log4j.LogManager
import java.io.File

class RecurringEntryImporter(private val recurringEntryFilePath: String?) {

    private val rer: RecurringEntryRepository = PersistenceServiceProvider.persistenceService.recurringEntryRepository

    fun importRecurringEntries() {

        val nanoTime = System.nanoTime()

        val file = findRecurringEntryImportPath()
        LOGGER.log(Level.INFO, "Started importing Recurring Entries from source: {}", file.path)

        val parsedRecurringEntries = parseRecurringEntries(file)
        persistParsedRecurringEntries(parsedRecurringEntries)

        LOGGER.log(
                Level.INFO,
                "Completed importing Recurring Entries successfully in {}. Recurring Entries ultimately in DB: {} ",
                DurationProvider.formatDurationSince(nanoTime), rer.findAllRecurringEntries().size)

    }

    private fun findRecurringEntryImportPath(): File {

        if (recurringEntryFilePath == null) {
            throw Exception("Import has been enabled without providing a file to import Recurring Entries from.")
        } else {
            return File(recurringEntryFilePath)
        }

    }


    private fun parseRecurringEntries(file: File): List<RecurringEntry> {

        val nanoTime = System.nanoTime()
        val recurringEntryParser: RecurringEntryParser = RecurringEntryCsvParser()

        val result = ArrayList<RecurringEntry>(1_000_000)

        var successful = 0
        var failed = 0

        for (current in LinesReader.readLinesFromFile(file)) {
            val parseResult = recurringEntryParser.extractEntry(current)

            // check on the potential failure
            if (parseResult.isPresent) {

                result.add(parseResult.get())
                successful++
            } else {
                failed++
            }
        }

        LOGGER.log(
                Level.INFO,
                "Parsing completed successfully in {}. Recurring Entries parsed: {} ({} skipped)",
                DurationProvider.formatDurationSince(nanoTime), successful, failed)

        return result
    }

    private fun persistParsedRecurringEntries(parsed: List<RecurringEntry>) {

        LOGGER.log(Level.INFO, "Started inserting parsed Recurring Entries into storage...")

        rer.saveAll(parsed)

        LOGGER.log(Level.INFO, "...finished persisting parsed Entries into storage.")
    }


    companion object {

        private val LOGGER = LogManager.getLogger(RecurringEntryParser::class.java)

    }

}
