@file:Suppress("JAVA_MODULE_DOES_NOT_DEPEND_ON_MODULE")

package de.omilke.banking.persistence

import de.omilke.banking.account.entity.Entry
import de.omilke.banking.account.entity.EntrySequence
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.time.LocalDate
import java.time.Month

class EntrySerializerTest {

    @Test
    fun testConversion() {

        val originalEntry = Entry(
                BigDecimal("11.99"),
                LocalDate.of(2020, Month.APRIL, 4),
                "This is my comment",
                "genericCategory",
                false,
                EntrySequence.LAST,
                23
        )

        val serialized = EntrySerializer.toPlain(originalEntry)

        val deserializedEntry = EntrySerializer.toEntry(serialized)

        assertThat(deserializedEntry.amount).isEqualByComparingTo(originalEntry.amount)
        assertThat(deserializedEntry.entryDate).isEqualTo(originalEntry.entryDate)
        assertThat(deserializedEntry.comment).isEqualTo(originalEntry.comment)
        assertThat(deserializedEntry.category).isEqualTo(originalEntry.category)
        assertThat(deserializedEntry.isSaving).isEqualTo(originalEntry.isSaving)
        assertThat(deserializedEntry.sequence).isEqualTo(originalEntry.sequence)
        assertThat(deserializedEntry.orderIndex).isEqualTo(originalEntry.orderIndex)
    }
}