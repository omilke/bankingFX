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

        val earlyEntry = Entry("", false, BigDecimal.ONE, earlyDate, false, "commentA")
        val sameDateEntry = Entry("", false, BigDecimal.ONE, earlyDate, false, "commentA")
        val lateEntry = Entry("", false, BigDecimal.ONE, laterDate, false, "commentA")

        val cut = SavingEntryComparator()

        //descending by date => late is smaller
        Assertions.assertThat(cut.compare(lateEntry, earlyEntry)).isLessThan(0)
        Assertions.assertThat(cut.compare(earlyEntry, lateEntry)).isGreaterThan(0)
        Assertions.assertThat(cut.compare(earlyEntry, sameDateEntry)).isEqualTo(0)
    }

    @Test
    fun testSortOrderByAmount() {

        val earlyDate = LocalDate.of(2021, 5, 4)

        val lowAmount = Entry("", false, BigDecimal.ZERO, earlyDate, false, "commentA")
        val highAmount = Entry("", false, BigDecimal.ONE, earlyDate, false, "commentA")
        val equallyLowAmount = Entry("", false, BigDecimal.ZERO, earlyDate, false, "commentA")

        val cut = SavingEntryComparator()

        //descending by date => late is smaller
        Assertions.assertThat(cut.compare(lowAmount, highAmount)).isLessThan(0)
        Assertions.assertThat(cut.compare(highAmount, lowAmount)).isGreaterThan(0)
        Assertions.assertThat(cut.compare(lowAmount, equallyLowAmount)).isEqualTo(0)
    }

    @Test
    fun testSortOrderByCategory() {

        val earlyDate = LocalDate.of(2021, 5, 4)

        val lowComment = Entry("", false, BigDecimal.ONE, earlyDate, false, "commentA")
        val highComment = Entry("", false, BigDecimal.ONE, earlyDate, false, "commentB")
        val equallyLowComment = Entry("", false, BigDecimal.ONE, earlyDate, false, "commentA")

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

        val laterEntryWithLowerAmount = Entry("", false, BigDecimal.ONE, laterDate, false, "commentB")
        val earlierEntryWithHigherAmount = Entry("", false, BigDecimal.ZERO, earlyDate, false, "commentB")

        val laterEntryWithLowerComment = Entry("", false, BigDecimal.ZERO, laterDate, false, "commentA")
        val earlierEntryWithHigherComment = Entry("", false, BigDecimal.ZERO, earlyDate, false, "commentB")

        val cut = SavingEntryComparator()

        //descending by date => late is smaller
        Assertions.assertThat(cut.compare(laterEntryWithLowerAmount, earlierEntryWithHigherAmount)).isLessThan(0)

        //descending by date => late is smaller
        Assertions.assertThat(cut.compare(laterEntryWithLowerComment, earlierEntryWithHigherComment)).isLessThan(0)
    }

}