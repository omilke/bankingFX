package de.omilke.bankingfx.main.entrylist.model

import de.omilke.banking.account.entity.EntrySequence
import java.time.LocalDate

/**
 * Provides total order of entries with respect to [EntrySequence] and the order index of [EntryTableRow] entity.
 *
 * @author Oliver Milke
 * @since 10.08.2015
 */
class EntryOrder constructor(private val date: LocalDate, val sequence: EntrySequence, val orderIndex: Int) : Comparable<EntryOrder> {

    val order: Long = calculateOrder(date, sequence, orderIndex)

    fun update(newSequence: EntrySequence, newOrderIndex: Int): EntryOrder {
        return EntryOrder(date, newSequence, newOrderIndex)
    }

    fun update(newDate: LocalDate): EntryOrder {
        return EntryOrder(newDate, sequence, orderIndex)
    }

    override fun compareTo(other: EntryOrder): Int {

        //TODO: this is actually just a hack to be able to use an integer comparator and could be solved with kotlins compareBy and selecting the attributes properly
        val difference = order - other.order
        return when {
            difference < 0 -> -1
            difference > 0 -> 1
            else -> 0
        }
    }

    companion object {

        @kotlin.jvm.JvmStatic
        fun of(date: LocalDate?, sequence: EntrySequence?, orderIndex: Int?): EntryOrder? {

            return when {
                date == null || sequence == null || orderIndex == null -> null
                else -> EntryOrder(date, sequence, orderIndex)
            }
        }

        @kotlin.jvm.JvmStatic
        fun calculateOrder(date: LocalDate, sequence: EntrySequence, orderIndex: Int): Long {

            // @formatter:off
            return (date.dayOfMonth
                    + orderIndex * 100L
                    + sequence.ordinal * 100L * 100
                    + date.monthValue * (100L * 100) * 10
                    + date.year * (100L * 100 * 10) * 100)
            // @formatter:on
        }
    }

}