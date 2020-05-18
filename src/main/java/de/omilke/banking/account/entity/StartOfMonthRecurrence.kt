package de.omilke.banking.account.entity

import java.time.LocalDate
import java.time.YearMonth

object StartOfMonthRecurrence : RecurrenceStrategy {

    override fun calculateFirstOccurrence(startOfPeriod: LocalDate): LocalDate {

        return if (startOfPeriod.dayOfMonth == 1) {
            startOfPeriod
        } else {
            calculateNextRecurrence(startOfPeriod)
        }
    }

    override fun calculateNextRecurrence(lastRecurrence: LocalDate): LocalDate {

        return YearMonth
                .of(lastRecurrence.year, lastRecurrence.month)
                .plusMonths(1)
                .atDay(1)
    }

}