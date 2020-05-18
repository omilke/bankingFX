package de.omilke.banking.account.entity

import java.math.BigDecimal
import java.time.LocalDate
import java.time.YearMonth

data class RecurringEntry(
        val amount: BigDecimal,
        val startOfRecurrence: LocalDate,
        var lastRecurrence: LocalDate?,
        val comment: String,
        val category: String,
        val isSaving: Boolean,
        val sequence: EntrySequence,
        val orderIndex: Int,
        val recurrenceStrategy: RecurrenceStrategy
) {

    fun generateRecurrenceFor(period: YearMonth, regenerate: Boolean = false): List<Entry> {

        return generateRecurrenceFor(period.atDay(1), period.atEndOfMonth(), regenerate)
    }

    fun generateRecurrenceFor(startOfPeriod: LocalDate, endOfPeriod: LocalDate, regenerate: Boolean = false): List<Entry> {

        val generatedEntries = ArrayList<Entry>()

        var recurrence = when {
            lastRecurrence == null -> recurrenceStrategy.calculateFirstOccurrence(startOfRecurrence)
            regenerate -> recurrenceStrategy.calculateFirstOccurrence(startOfRecurrence)
            else -> recurrenceStrategy.calculateNextRecurrence(lastRecurrence(regenerate))
        }

        while (recurrenceIsBeforeEndOfPeriod(recurrence, endOfPeriod)) {

            //especially if re-generation was requested, a recurrence might not yet be in the requested period
            if (recurrenceIsWithinPeriod(startOfPeriod, endOfPeriod, recurrence)) {

                generatedEntries += Entry(amount, recurrence, comment, category, isSaving, sequence, orderIndex)
                lastRecurrence = recurrence
            }

            //continue to generate and check whether the new recurrence still matches the requested period
            recurrence = this.recurrenceStrategy.calculateNextRecurrence(recurrence)
        }

        return generatedEntries
    }

    private fun lastRecurrence(regenerate: Boolean): LocalDate {

        return if (regenerate) {
            startOfRecurrence
        } else {
            //null must have been passed in the condition in order for this branch to be executed
            lastRecurrence!!
        }
    }

    private fun recurrenceIsWithinPeriod(startOfPeriod: LocalDate, endOfPeriod: LocalDate, recurrence: LocalDate): Boolean {

        return recurrence in startOfPeriod..endOfPeriod
    }

    private fun recurrenceIsBeforeEndOfPeriod(recurrence: LocalDate, endOfPeriod: LocalDate): Boolean {

        return recurrence <= endOfPeriod
    }
}