package de.omilke.banking.persistence.jooq.repositories

import de.omilke.banking.account.entity.RecurringEntry
import de.omilke.banking.account.entity.RecurringEntryRepository
import de.omilke.banking.persistence.jooq.JooqQueryExecutor
import de.omilke.util.DurationProvider
import org.apache.logging.log4j.Level
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

internal class RecurringEntryRepositoryJooq(private val queryExecutor: JooqQueryExecutor) : RecurringEntryRepository {

    /**
     * Creates a repository instance that makes use of the default Query Executor.
     */
    constructor() : this(JooqQueryExecutor())

    override fun findAllRecurringEntries(): MutableList<RecurringEntry> {
        val nanoTime = System.nanoTime()
        val result = queryExecutor.selectRecurringEntries()

        LOGGER.log(Level.INFO, "fetching ${result.size} recurring entries took {}", DurationProvider.formatDurationSince(nanoTime))

        return result
    }

    override fun saveAll(recurringEntries: Collection<RecurringEntry>) {

        val nanoTime = System.nanoTime()

        for (recurringEntry in recurringEntries) {
            queryExecutor.persist(recurringEntry)
        }

        queryExecutor.commit()

        LOGGER.log(Level.INFO, "persisting ${recurringEntries.size} recurring entries took {}", DurationProvider.formatDurationSince(nanoTime))
    }

    companion object {

        val LOGGER: Logger = LogManager.getLogger(RecurringEntryRepositoryJooq::class.java)
    }
}