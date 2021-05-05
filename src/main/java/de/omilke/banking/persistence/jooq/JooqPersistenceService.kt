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

    override val entryRepository: EntryRepository = EntryRepositoryJooq()
    override val recurringEntryRepository: RecurringEntryRepository = RecurringEntryRepositoryJooq()
    override val securityRepository: SecurityRepository = SecurityRepositoryHardCoded()

    override fun checkPersistenceLayerReadiness() {

        queryExecutor.checkPersistenceLayerReadiness()
    }

    override fun purgeAndPrepareStorage() {

        queryExecutor.purgeAndPrepareDatabase()
    }

}