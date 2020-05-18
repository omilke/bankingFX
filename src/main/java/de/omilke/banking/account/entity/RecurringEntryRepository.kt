package de.omilke.banking.account.entity

interface RecurringEntryRepository {

    fun findAllRecurringEntries(): MutableList<RecurringEntry>

    fun saveAll(recurringEntries: Collection<RecurringEntry>)
}