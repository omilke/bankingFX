package de.omilke.banking.account.entity

import java.time.LocalDate
import java.time.YearMonth

object EndOfMonthRecurrence : RecurrenceStrategy {

    override fun calculateFirstOccurrence(startOfPeriod: LocalDate): LocalDate {

        return endOfMonthDate(startOfPeriod)
    }

    override fun calculateNextRecurrence(lastRecurrence: LocalDate): LocalDate {

        return if (lastRecurrence.dayOfMonth == lastRecurrence.lengthOfMonth()) {
            YearMonth.of(lastRecurrence.year, lastRecurrence.month)
                    .plusMonths(1)
                    .atEndOfMonth()
        } else {
            endOfMonthDate(lastRecurrence)
        }
    }

    private fun endOfMonthDate(startOfPeriod: LocalDate) =
            LocalDate.of(startOfPeriod.year, startOfPeriod.month, startOfPeriod.lengthOfMonth())
}