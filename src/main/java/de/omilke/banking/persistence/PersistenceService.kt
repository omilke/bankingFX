package de.omilke.banking.persistence

import de.omilke.banking.account.entity.EntryRepository
import de.omilke.banking.account.entity.RecurringEntryRepository

interface PersistenceService {

    fun checkPersistenceLayerReadiness()

    fun purgeAndPrepareStorage()

    val entryRepository: EntryRepository

    val recurringEntryRepository: RecurringEntryRepository
}