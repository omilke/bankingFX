@file:Suppress("JAVA_MODULE_DOES_NOT_DEPEND_ON_MODULE")

package de.omilke.banking.persistence.jooq.mapper

import de.omilke.banking.account.entity.EndOfMonthRecurrence
import de.omilke.banking.account.entity.RecurrenceStrategy
import de.omilke.banking.account.entity.StartOfMonthRecurrence
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class StrategyMapperTest {

    @Test
    fun testThatStrategiesCanBeConvertedBackAndForth() {

        val strategies = ArrayList<RecurrenceStrategy>().apply {
            add(StartOfMonthRecurrence)
            add(EndOfMonthRecurrence)
        }

        for (strategy in strategies) {

            val labelForStrategy = StrategyMapper.getLabelForStrategy(strategy)
            assertThat(StrategyMapper.getStrategyFor(labelForStrategy)).isEqualTo(strategy)
        }

    }
}