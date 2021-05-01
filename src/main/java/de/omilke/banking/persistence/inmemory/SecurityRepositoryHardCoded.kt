package de.omilke.banking.persistence.inmemory

import de.omilke.banking.account.entity.Security
import de.omilke.banking.account.entity.SecurityRepository

class SecurityRepositoryHardCoded: SecurityRepository {

    override fun findAllSecurity(): MutableList<Security> {
        return ArrayList()
    }
}