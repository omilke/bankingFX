@file:Suppress("JAVA_MODULE_DOES_NOT_DEPEND_ON_MODULE")

package de.omilke.banking.account.entity

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDate

internal class EndOfMonthRecurrenceTest {

    @Test
    fun testFirstOccurrenceAlwaysIsLastDayOfCurrentMonth() {

        var firstOccurrence = EndOfMonthRecurrence.calculateFirstOccurrence(LocalDate.of(2020, 4, 1))
        assertThat(firstOccurrence).isEqualTo(LocalDate.of(2020, 4, 30))

        firstOccurrence = EndOfMonthRecurrence.calculateFirstOccurrence(LocalDate.of(2020, 4, 30))
        assertThat(firstOccurrence).isEqualTo(LocalDate.of(2020, 4, 30))
    }

    @Test
    fun testNextRecurrenceIsEndOfNextMonthIfLastRecurrenceWasLastDayOfMonth() {

        var nextRecurrence = EndOfMonthRecurrence.calculateNextRecurrence(LocalDate.of(2020, 4, 29))
        assertThat(nextRecurrence).isEqualTo(LocalDate.of(2020, 4, 30))

        //make sure that new year is not a problem
        nextRecurrence = EndOfMonthRecurrence.calculateNextRecurrence(LocalDate.of(2020, 12, 31))
        assertThat(nextRecurrence).isEqualTo(LocalDate.of(2021, 1, 31))

        //also make sure there is nothing weird about leap year
        nextRecurrence = EndOfMonthRecurrence.calculateNextRecurrence(LocalDate.of(2024, 1, 31))
        assertThat(nextRecurrence).isEqualTo(LocalDate.of(2024, 2, 29))
    }

    @Test
    fun testNextRecurrenceIsEndOfCurrentMonthIfLastRecurrenceWasNotLastDayOfMonth() {

        var nextRecurrence = EndOfMonthRecurrence.calculateNextRecurrence(LocalDate.of(2020, 4, 29))
        assertThat(nextRecurrence).isEqualTo(LocalDate.of(2020, 4, 30))

        //again check leap year as well
        nextRecurrence = EndOfMonthRecurrence.calculateNextRecurrence(LocalDate.of(2024, 2, 28))
        assertThat(nextRecurrence).isEqualTo(LocalDate.of(2024, 2, 29))
    }

}