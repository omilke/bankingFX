package de.omilke.banking.account.entity

interface SecurityRepository {

    fun findAllSecurity(): MutableList<Security>
}