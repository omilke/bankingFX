package de.omilke.banking.interop.importing

import de.omilke.banking.account.entity.Entry
import de.omilke.banking.account.entity.EntryRepository
import de.omilke.banking.interop.importing.io.LinesReader
import de.omilke.banking.interop.importing.parser.jhaushalt.JHaushaltParser
import de.omilke.banking.persistence.PersistenceServiceProvider
import de.omilke.util.DurationProvider
import org.apache.logging.log4j.Level
import org.apache.logging.log4j.LogManager
import java.io.File
import java.nio.charset.StandardCharsets

class EntryImporter(private val entryFilePath: String?, private val entryOrderFilePath: String?, private val entrySubCategoryFilePath: String?) {

    private val er: EntryRepository = PersistenceServiceProvider.persistenceService.entryRepository

    fun importEntries() {

        val nanoTime = System.nanoTime()

        val file = findEntryImportPath()
        LOGGER.log(Level.INFO, "Started importing Entries from source: {}", file.path)

        val parsedEntries = parseEntries(file)
        persistParsedEntries(parsedEntries)

        LOGGER.log(
                Level.INFO,
                "Completed importing Entries successfully in {}. Entries ultimately in DB: {} ",
                DurationProvider.formatDurationSince(nanoTime), er.findAllEntries().size)
    }

    private fun findEntryImportPath(): File {

        if (entryFilePath == null) {
            throw Exception("Import has been enabled without providing a file to import Entries from.")
        } else {
            return File(entryFilePath)
        }

    }

    private fun parseEntries(file: File): List<Entry> {

        val nanoTime = System.nanoTime()

        val entryParser = JHaushaltParser(
                entryOrderFilePath,
                entrySubCategoryFilePath)

        val result = ArrayList<Entry>(1_000_000)

        var successful = 0
        var failed = 0

        for (current in LinesReader.readLinesFromFile(file, StandardCharsets.ISO_8859_1)) {
            val parsedEntry = entryParser.extractEntry(current)

            // check on the potential failure
            if (parsedEntry.isPresent) {

                //TODO: once there is category class, a cache should introduced making sure categories with same name actually are the same
                result.add(parsedEntry.get())
                successful++
            } else {
                failed++
            }
        }

        LOGGER.log(
                Level.INFO,
                "Parsing completed successfully in {}. Entries parsed: {} ({} skipped)",
                DurationProvider.formatDurationSince(nanoTime), successful, failed)

        return result
    }


    private fun persistParsedEntries(parsed: List<Entry>) {

        LOGGER.log(Level.INFO, "Started inserting parsed Entries into storage...")

        er.saveAll(parsed)

        LOGGER.log(Level.INFO, "...finished persisting parsed Entries into storage.")
    }

    companion object {

        private val LOGGER = LogManager.getLogger(EntryImporter::class.java)
    }

}