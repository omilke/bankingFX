package de.omilke.banking.interop.importing

import de.omilke.banking.BankingConfigurator
import de.omilke.banking.ConfigurationConstants.IMPORT_ACTIVE
import de.omilke.banking.ConfigurationConstants.IMPORT_ENTRIES_PATH
import de.omilke.banking.ConfigurationConstants.IMPORT_ENTRY_CATEGORIES_PATH
import de.omilke.banking.ConfigurationConstants.IMPORT_ENTRY_ORDER_PATH
import de.omilke.banking.ConfigurationConstants.IMPORT_RECURRINGENTRIES_PATH
import de.omilke.banking.persistence.PersistenceServiceProvider.persistenceService
import de.omilke.util.DurationProvider
import org.apache.logging.log4j.Level
import org.apache.logging.log4j.LogManager

class Importer {

    private val configuration = BankingConfigurator.getConfigurationFor(
            listOf(
                    IMPORT_ACTIVE,
                    IMPORT_ENTRIES_PATH,
                    IMPORT_ENTRY_ORDER_PATH,
                    IMPORT_ENTRY_CATEGORIES_PATH,
                    IMPORT_RECURRINGENTRIES_PATH))

    private fun process() {

        if (importIsEnabled()) {

            LOGGER.log(Level.INFO, "Started importing data.")
            val nanoTime = System.nanoTime()

            persistenceService.purgeAndPrepareStorage()

            importEntries()

            importRecurringEntries()

            LOGGER.log(
                    Level.INFO,
                    "Completed importing data successfully after {}.",
                    DurationProvider.formatDurationSince(nanoTime))

        } else {
            LOGGER.info("Skipped import of file due to configuration")
        }
    }

    private fun importEntries() {

        EntryImporter(
                configuration[IMPORT_ENTRIES_PATH],
                configuration[IMPORT_ENTRY_ORDER_PATH],
                configuration[IMPORT_ENTRY_CATEGORIES_PATH])
                .importEntries()
    }

    private fun importRecurringEntries() {

        RecurringEntryImporter(configuration[IMPORT_RECURRINGENTRIES_PATH])
                .importRecurringEntries()
    }

    private fun importIsEnabled(): Boolean {

        return BankingConfigurator.isPropertyEnabled(IMPORT_ACTIVE)
    }

    companion object {

        private val LOGGER = LogManager.getLogger(Importer::class.java)

        fun doImport() {
            Importer().process()
        }
    }
}