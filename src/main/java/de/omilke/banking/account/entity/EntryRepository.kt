package de.omilke.banking.account.entity

import java.time.LocalDate

interface EntryRepository {

    fun update(entry: Entry): Entry

    fun findAllEntries(): MutableList<Entry>

    fun findAllEntriesBetweenWithCategoryName(
            start: LocalDate? = null,
            end: LocalDate? = null,
            name: String? = null)
            : MutableList<Entry>

    fun saveAll(entries: Collection<Entry>)
}