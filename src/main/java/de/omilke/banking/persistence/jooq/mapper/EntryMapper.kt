package de.omilke.banking.persistence.jooq.mapper

import de.omilke.banking.account.entity.Entry
import de.omilke.banking.account.entity.EntrySequence
import de.omilke.banking.persistence.jooq.meta.tables.records.EntryRecord
import org.jooq.RecordMapper
import org.jooq.exception.InvalidResultException

object EntryMapper : RecordMapper<EntryRecord, Entry> {

    override fun map(record: EntryRecord): Entry {

        val sequence = EntrySequence.of(record.sequence)
                ?: throw InvalidResultException("Entry read from database does not contain valid sequence. Found: " + record.sequence)

        return Entry(
                record.amount,
                record.entrydate,
                record.comment,
                record.category,
                record.saving,
                sequence,
                record.orderindex
        )

    }

}
