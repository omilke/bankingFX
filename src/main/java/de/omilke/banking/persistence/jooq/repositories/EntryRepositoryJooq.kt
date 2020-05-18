package de.omilke.banking.persistence.jooq.repositories

import de.omilke.banking.account.entity.Entry
import de.omilke.banking.account.entity.EntryRepository
import de.omilke.banking.persistence.jooq.JooqQueryExecutor
import de.omilke.util.DurationProvider
import org.apache.logging.log4j.Level
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import java.time.LocalDate

internal class EntryRepositoryJooq(private val queryExecutor: JooqQueryExecutor) : EntryRepository {

    /**
     * Creates a repository instance that makes use of the default Query Executor.
     */
    constructor() : this(JooqQueryExecutor())

    override fun update(entry: Entry): Entry {
        TODO("Not yet implemented")
    }

    override fun saveAll(entries: Collection<Entry>) {

        val nanoTime = System.nanoTime()

        for (entry in entries) {
            queryExecutor.persist(entry)
        }

        queryExecutor.commit()

        LOGGER.log(Level.INFO, "persisting ${entries.size} entries took {}", DurationProvider.formatDurationSince(nanoTime))
    }

    override fun findAllEntries(): MutableList<Entry> {

        return findAllEntriesBetweenWithCategoryName(null, null, null)
    }

    override fun findAllEntriesBetweenWithCategoryName(start: LocalDate?, end: LocalDate?, name: String?): MutableList<Entry> {

        val nanoTime = System.nanoTime()
        val result = queryExecutor.selectEntries(start, end, name)

        LOGGER.log(Level.INFO, "fetching ${result.size} entries took {}", DurationProvider.formatDurationSince(nanoTime))

        return result
    }

    companion object {

        val LOGGER: Logger = LogManager.getLogger(EntryRepositoryJooq::class.java)
    }

}
