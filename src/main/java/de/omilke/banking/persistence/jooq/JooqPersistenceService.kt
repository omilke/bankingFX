package de.omilke.banking.persistence.jooq

import de.omilke.banking.account.entity.EntryRepository
import de.omilke.banking.account.entity.RecurringEntryRepository
import de.omilke.banking.account.entity.SecurityRepository
import de.omilke.banking.persistence.PersistenceService
import de.omilke.banking.persistence.inmemory.SecurityRepositoryHardCoded
import de.omilke.banking.persistence.jooq.repositories.EntryRepositoryJooq
import de.omilke.banking.persistence.jooq.repositories.RecurringEntryRepositoryJooq

object JooqPersistenceService : PersistenceService {

    private val queryExecutor = JooqQueryExecutor()

    private val _entryRepository = EntryRepositoryJooq()
    private val _recurringEntryRepository = RecurringEntryRepositoryJooq()
    private val _securityRepository = SecurityRepositoryHardCoded()

    override val entryRepository: EntryRepository
        get() = _entryRepository

    override val recurringEntryRepository: RecurringEntryRepository
        get() = _recurringEntryRepository

    override val securityRepository: SecurityRepository
        get() = _securityRepository

    override fun checkPersistenceLayerReadiness() {

        queryExecutor.checkPersistenceLayerReadiness()
    }

    override fun purgeAndPrepareStorage() {

        queryExecutor.purgeAndPrepareDatabase()
    }

}