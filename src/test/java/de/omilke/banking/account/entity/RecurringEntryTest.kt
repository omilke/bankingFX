@file:Suppress("JAVA_MODULE_DOES_NOT_DEPEND_ON_MODULE")

package de.omilke.banking.account.entity

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.time.LocalDate
import java.time.YearMonth

class RecurringEntryTest {

    private val startOfRecurrence = LocalDate.of(2020, 4, 1)
    private val april2020 = YearMonth.of(2020, 4)
    private val december2020 = YearMonth.of(2020, 12)

    private val referenceLastRecurrence = null

    private val referenceAmount = BigDecimal("123.45")
    private val referenceComment = "myComment"
    private val referenceCategory = "myCategory"
    private val referenceIsSaving = true
    private val referenceSequence = EntrySequence.FIRST
    private val referenceOrderIndex = 3

    @Test
    fun testThatRecurrenceNeverIsBeforeStartOfPeriod() {

        val listOfRecurredEntries = recurringEntryOf(startOfRecurrence, StartOfMonthRecurrence).generateRecurrenceFor(YearMonth.of(2020, 3))
        assertThat(listOfRecurredEntries).hasSize(0)
    }


    @Test
    fun testThatRecurrenceIsAlwaysAtFirstOccurrenceWhenInitiallyGenerated() {

        val strategies = listOf(StartOfMonthRecurrence, EndOfMonthRecurrence)

        for (currentStrategy in strategies) {

            val recurringEntry = recurringEntryOf(startOfRecurrence, currentStrategy)

            val listOfRecurredEntries = recurringEntry.generateRecurrenceFor(YearMonth.of(2020, 4))
            assertThat(listOfRecurredEntries).hasSize(1)
            assertThat(listOfRecurredEntries[0].entryDate).isEqualTo(currentStrategy.calculateFirstOccurrence(startOfRecurrence))

            assertThat(recurringEntry.lastRecurrence).isEqualTo(currentStrategy.calculateFirstOccurrence(startOfRecurrence))
        }
    }

    @Test
    fun testThatRecurredEntryPropertiesAreAsExpected() {

        //expect one Entry having the start of recurrence
        val listOfRecurredEntries = recurringEntryOf(startOfRecurrence, StartOfMonthRecurrence).generateRecurrenceFor(YearMonth.of(2020, 4))
        assertThat(listOfRecurredEntries).hasSize(1)

        val generatedEntry = listOfRecurredEntries[0]

        assertThat(generatedEntry.amount).isEqualByComparingTo(referenceAmount)
        assertThat(generatedEntry.entryDate).isEqualTo(LocalDate.of(2020, 4, 1))
        assertThat(generatedEntry.category).isEqualTo(referenceCategory)
        assertThat(generatedEntry.comment).isEqualTo(referenceComment)
        assertThat(generatedEntry.isSaving).isEqualTo(referenceIsSaving)
        assertThat(generatedEntry.orderIndex).isEqualTo(referenceOrderIndex)
        assertThat(generatedEntry.sequence).isEqualTo(referenceSequence)
    }

    @Test
    fun testThatRecurrenceIsProperlyGeneratedForSkippedPeriods() {

        val listOfRecurredEntries = recurringEntryOf(startOfRecurrence, EndOfMonthRecurrence).generateRecurrenceFor(december2020)
        assertThat(listOfRecurredEntries).hasSize(1)
    }

    @Test
    fun testThatRecurrencesAreProperlyGeneratedOverALongerPeriodWhileStillRespectingStartOfRecurrence() {

        val startOfPeriod = LocalDate.of(2020, 1, 1)
        val endOfPeriod = LocalDate.of(2020, 12, 31)

        val listOfRecurredEntries = recurringEntryOf(startOfRecurrence, StartOfMonthRecurrence).generateRecurrenceFor(startOfPeriod, endOfPeriod)
        assertThat(listOfRecurredEntries).hasSize(9)

        assertThat(listOfRecurredEntries[0].entryDate).isEqualTo(LocalDate.of(2020, 4, 1))
        assertThat(listOfRecurredEntries[1].entryDate).isEqualTo(LocalDate.of(2020, 5, 1))
        assertThat(listOfRecurredEntries[2].entryDate).isEqualTo(LocalDate.of(2020, 6, 1))
        assertThat(listOfRecurredEntries[3].entryDate).isEqualTo(LocalDate.of(2020, 7, 1))
        assertThat(listOfRecurredEntries[4].entryDate).isEqualTo(LocalDate.of(2020, 8, 1))
        assertThat(listOfRecurredEntries[5].entryDate).isEqualTo(LocalDate.of(2020, 9, 1))
        assertThat(listOfRecurredEntries[6].entryDate).isEqualTo(LocalDate.of(2020, 10, 1))
        assertThat(listOfRecurredEntries[7].entryDate).isEqualTo(LocalDate.of(2020, 11, 1))
        assertThat(listOfRecurredEntries[8].entryDate).isEqualTo(LocalDate.of(2020, 12, 1))
    }

    @Test
    fun testThatRecurrenceIsProperlyGeneratedAccordingToLastGeneratedRecurrenceAndRegenerateFlagAtStartOfRecurrence() {

        val recurringEntry = recurringEntryOf(startOfRecurrence, StartOfMonthRecurrence)

        var listOfRecurredEntries = recurringEntry.generateRecurrenceFor(april2020)
        assertThat(listOfRecurredEntries).hasSize(1)

        //but also make sure not to generate the same entry more than once...
        listOfRecurredEntries = recurringEntry.generateRecurrenceFor(april2020)
        assertThat(listOfRecurredEntries).hasSize(0)

        //...that is unless it is explicitly requested
        listOfRecurredEntries = recurringEntry.generateRecurrenceFor(april2020, true)
        assertThat(listOfRecurredEntries).hasSize(1)

        //...make sure last generated is saved correctly when used with regenerate
        listOfRecurredEntries = recurringEntry.generateRecurrenceFor(april2020)
        assertThat(listOfRecurredEntries).hasSize(0)
    }

    private fun recurringEntryOf(startOfRecurrence: LocalDate, recurrenceStrategy: RecurrenceStrategy): RecurringEntry {

        return RecurringEntry(
                referenceAmount,
                startOfRecurrence,
                referenceLastRecurrence,
                referenceComment,
                referenceCategory,
                referenceIsSaving,
                referenceSequence,
                referenceOrderIndex,
                recurrenceStrategy)
    }
}