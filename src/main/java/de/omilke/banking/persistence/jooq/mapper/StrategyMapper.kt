package de.omilke.banking.persistence.jooq.mapper

import de.omilke.banking.account.entity.EndOfMonthRecurrence
import de.omilke.banking.account.entity.RecurrenceStrategy
import de.omilke.banking.account.entity.StartOfMonthRecurrence

object StrategyMapper {

    private val strategies by lazy {

        HashMap<String, RecurrenceStrategy>().apply {
            put(EndOfMonthRecurrence.javaClass.simpleName, EndOfMonthRecurrence)
            put(StartOfMonthRecurrence.javaClass.simpleName, StartOfMonthRecurrence)
        }
    }

    fun getStrategyFor(strategyLabel: String): RecurrenceStrategy? {

        return strategies[strategyLabel]
    }

    fun getLabelForStrategy(strategy: RecurrenceStrategy): String {

        return strategy.javaClass.simpleName
    }
}