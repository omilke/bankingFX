package de.omilke.banking.persistence

import de.omilke.banking.persistence.jooq.JooqPersistenceService

/**
 * Provides the app-wide Persistence Service.
 */
object PersistenceServiceProvider {

    val persistenceService: PersistenceService = JooqPersistenceService

}
