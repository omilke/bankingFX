package de.omilke.banking.account

import de.omilke.banking.account.entity.Entry
import de.omilke.banking.persistence.PersistenceServiceProvider
import java.util.stream.Collectors

object EntryService {

    private val er = PersistenceServiceProvider.persistenceService.entryRepository

    fun getAllCategories(): List<String> {

        return er.findAllEntries()
                .stream()
                .map(Entry::category)
                .distinct()
                .collect(Collectors.toList())
    }

}