@file:Suppress("JAVA_MODULE_DOES_NOT_DEPEND_ON_MODULE")

package de.omilke.banking.persistence.jooq.repositories

import de.omilke.banking.account.entity.EndOfMonthRecurrence
import de.omilke.banking.account.entity.EntrySequence
import de.omilke.banking.account.entity.RecurringEntry
import de.omilke.banking.persistence.jooq.JooqContext
import de.omilke.banking.persistence.jooq.JooqQueryExecutor
import org.assertj.core.api.Assertions.assertThat
import org.jooq.SQLDialect
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.time.LocalDate
import java.util.*

@Tag("IntegrationTest")
class RecurringEntryRepositoryJooqTest {

    private val jooqContext = JooqContext("user", "password", "jdbc:h2:./target/test.bankingfx.h2", SQLDialect.H2)

    private val queryExecutor = JooqQueryExecutor(jooqContext)
    private val cut = RecurringEntryRepositoryJooq(queryExecutor)

    @BeforeEach
    fun prepareDb() {

        queryExecutor.purgeAndPrepareDatabase()
    }

    @Test
    fun testThatRecurringEntryCanBePersistedAndLoadedAgain() {

        val recurringEntries = ArrayList<RecurringEntry>().apply {

            add(RecurringEntry(BigDecimal.TEN, LocalDate.now(), null, "my recurring entry", "Sonstiges", false, EntrySequence.REGULAR, 0, EndOfMonthRecurrence))
        }

        cut.saveAll(recurringEntries)

        val queriedEntries = cut.findAllRecurringEntries()
        assertThat(queriedEntries).hasSize(1)
    }
}