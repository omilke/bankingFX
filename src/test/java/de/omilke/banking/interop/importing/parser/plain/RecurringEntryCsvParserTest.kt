@file:Suppress("JAVA_MODULE_DOES_NOT_DEPEND_ON_MODULE")

package de.omilke.banking.interop.importing.parser.plain

import de.omilke.banking.account.entity.EndOfMonthRecurrence
import de.omilke.banking.account.entity.EntrySequence
import de.omilke.banking.account.entity.RecurringEntry
import de.omilke.banking.account.entity.StartOfMonthRecurrence
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.time.LocalDate
import java.util.*

internal class RecurringEntryCsvParserTest {

    //TODO: remove privacy details

    @Test
    fun extractEntry() {

        val cut = RecurringEntryCsvParser()

        verify(
                cut.extractEntry("5.23;2020-04-01;Gehalt;Einkommen;FALSE;LAST;23;EndOfMonthRecurrence"),
                RecurringEntry(BigDecimal("5.23"), LocalDate.of(2020, 4, 1), null, "Gehalt", "Einkommen", false, EntrySequence.LAST, 23, EndOfMonthRecurrence)
        )

        verify(
                cut.extractEntry("-5.99;2020-04-01;Handschuhe;Sport:Equipment;TRUE;REGULAR;0;StartOfMonthRecurrence"),
                RecurringEntry(BigDecimal("-5.99"), LocalDate.of(2020, 4, 1), null, "Handschuhe", "Sport:Equipment", true, EntrySequence.REGULAR, 0, StartOfMonthRecurrence)
        )
    }

    private fun verify(result: Optional<RecurringEntry>, expected: RecurringEntry) {

        assertThat(result.isPresent).isTrue()
        assertThat(result.get()).isEqualTo(expected)
    }
}