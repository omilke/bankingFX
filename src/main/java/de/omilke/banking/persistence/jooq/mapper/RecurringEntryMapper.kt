package de.omilke.banking.persistence.jooq.mapper

import de.omilke.banking.account.entity.EntrySequence
import de.omilke.banking.account.entity.RecurringEntry
import de.omilke.banking.persistence.jooq.meta.tables.records.RecurringEntryRecord
import org.jooq.RecordMapper
import org.jooq.exception.InvalidResultException

object RecurringEntryMapper : RecordMapper<RecurringEntryRecord, RecurringEntry> {

    override fun map(record: RecurringEntryRecord): RecurringEntry {

        val sequence = EntrySequence.of(record.sequence)
                ?: throw InvalidResultException("RecurringEntry read from database does not contain valid sequence. Found: " + record.sequence)

        val recurrenceStrategy = StrategyMapper.getStrategyFor(record.recurrencestrategy!!)
                ?: throw InvalidResultException("RecurringEntry read from database does not contain valid recurrence strategy. Found: " + record.recurrencestrategy)

        //https://github.com/jOOQ/jOOQ/issues/10212 states that types from SQL could generally be nullable
        //however, since they can only be persisted from the Domain Model, which inhibits, null, we can assume values are non-null here
        //BUT this actually hinges on the encapsulation of the DB write
        return RecurringEntry(
                record.amount!!,
                record.startofrecurrence!!,
                record.lastrecurrence,
                record.comment!!,
                record.category!!,
                record.saving!!,
                sequence,
                record.orderindex!!,
                recurrenceStrategy
        )
    }

}
