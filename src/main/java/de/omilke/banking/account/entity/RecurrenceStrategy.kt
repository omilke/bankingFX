package de.omilke.banking.account.entity

import java.time.LocalDate

interface RecurrenceStrategy {

    fun calculateFirstOccurrence(startOfPeriod: LocalDate): LocalDate

    fun calculateNextRecurrence(lastRecurrence: LocalDate): LocalDate

}
