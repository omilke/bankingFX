@file:Suppress("JAVA_MODULE_DOES_NOT_DEPEND_ON_MODULE")

package de.omilke.banking.account.entity

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDate

class StartOfMonthRecurrenceTest {

    @Test
    fun testCalculateFirstOccurenceIsStartOfPeriodIfFirstDayOfMonth() {

        val firstOccurrence = StartOfMonthRecurrence.calculateFirstOccurrence(LocalDate.of(2020, 4, 1))
        assertThat(firstOccurrence).isEqualTo(LocalDate.of(2020, 4, 1))
    }

    @Test
    fun testCalculateFirstOccurenceIsAtStartOfNextMonthWhenStartOfPeriodIsNotFirstDayOfMonth() {

        val firstOccurrence = StartOfMonthRecurrence.calculateFirstOccurrence(LocalDate.of(2020, 4, 2))
        assertThat(firstOccurrence).isEqualTo(LocalDate.of(2020, 5, 1))
    }

    @Test
    fun testCalculateNextRecurrenceIsAlwaysAtFirstDayOfMonth() {

        val nextRecurrence = StartOfMonthRecurrence.calculateNextRecurrence(LocalDate.of(2020, 4, 30))
        assertThat(nextRecurrence).isEqualTo(LocalDate.of(2020, 5, 1))
    }

    @Test
    fun testCalculateNextRecurrenceHandlesYearBoundary() {

        val nextRecurrence = StartOfMonthRecurrence.calculateNextRecurrence(LocalDate.of(2020, 12, 1))
        assertThat(nextRecurrence).isEqualTo(LocalDate.of(2021, 1, 1))

    }
}