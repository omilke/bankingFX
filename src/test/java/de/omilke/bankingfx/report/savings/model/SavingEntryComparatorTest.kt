@file:Suppress("JAVA_MODULE_DOES_NOT_DEPEND_ON_MODULE")
package de.omilke.bankingfx.report.savings.model

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.time.LocalDate

internal class SavingEntryComparatorTest {

    @Test
    fun testSortOrderByDateDescending() {

        val earlyDate = LocalDate.of(2021, 5, 4)
        val laterDate = LocalDate.of(2021, 5, 6)

        val earlyEntry = Entry(earlyDate, BigDecimal.ONE, false, "category", "commentA")
        val sameDateEntry = Entry(earlyDate, BigDecimal.ONE, false, "category", "commentA")
        val lateEntry = Entry(laterDate, BigDecimal.ONE, false, "category", "commentA")

        val cut = SavingEntryComparator()

        //descending by date => late is smaller
        Assertions.assertThat(cut.compare(lateEntry, earlyEntry)).isLessThan(0)
        Assertions.assertThat(cut.compare(earlyEntry, lateEntry)).isGreaterThan(0)
        Assertions.assertThat(cut.compare(earlyEntry, sameDateEntry)).isEqualTo(0)
    }

    @Test
    fun testSortOrderByAmount() {

        val earlyDate = LocalDate.of(2021, 5, 4)

        val lowAmount = Entry(earlyDate, BigDecimal.ZERO, false, "category", "commentA")
        val highAmount = Entry(earlyDate, BigDecimal.ONE, false, "category", "commentA")
        val equallyLowAmount = Entry(earlyDate, BigDecimal.ZERO, false, "category", "commentA")

        val cut = SavingEntryComparator()

        //descending by date => late is smaller
        Assertions.assertThat(cut.compare(lowAmount, highAmount)).isLessThan(0)
        Assertions.assertThat(cut.compare(highAmount, lowAmount)).isGreaterThan(0)
        Assertions.assertThat(cut.compare(lowAmount, equallyLowAmount)).isEqualTo(0)
    }

    @Test
    fun testSortOrderByCategory() {

        val earlyDate = LocalDate.of(2021, 5, 4)

        val lowComment = Entry(earlyDate, BigDecimal.ONE, false, "category", "commentA")
        val highComment = Entry(earlyDate, BigDecimal.ONE, false, "category", "commentB")
        val equallyLowComment = Entry(earlyDate, BigDecimal.ONE, false, "category", "commentA")

        val cut = SavingEntryComparator()

        //descending by date => late is smaller
        Assertions.assertThat(cut.compare(lowComment, highComment)).isLessThan(0)
        Assertions.assertThat(cut.compare(highComment, lowComment)).isGreaterThan(0)
        Assertions.assertThat(cut.compare(lowComment, equallyLowComment)).isEqualTo(0)
    }

    @Test
    fun testSortPriority() {

        val earlyDate = LocalDate.of(2021, 5, 4)
        val laterDate = LocalDate.of(2021, 5, 6)

        val laterEntryWithLowerAmount = Entry(laterDate, BigDecimal.ONE, false, "category", "commentB")
        val earlierEntryWithHigherAmount = Entry(earlyDate, BigDecimal.ZERO, false, "category", "commentB")

        val laterEntryWithLowerComment = Entry(laterDate, BigDecimal.ZERO, false, "category", "commentA")
        val earlierEntryWithHigherComment = Entry(earlyDate, BigDecimal.ZERO, false, "category", "commentB")

        val cut = SavingEntryComparator()

        //descending by date => late is smaller
        Assertions.assertThat(cut.compare(laterEntryWithLowerAmount, earlierEntryWithHigherAmount)).isLessThan(0)

        //descending by date => late is smaller
        Assertions.assertThat(cut.compare(laterEntryWithLowerComment, earlierEntryWithHigherComment)).isLessThan(0)
    }



}