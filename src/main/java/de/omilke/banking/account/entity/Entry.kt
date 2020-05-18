package de.omilke.banking.account.entity

import java.math.BigDecimal
import java.time.LocalDate
import java.util.*

data class Entry(
        var amount: BigDecimal,
        var entryDate: LocalDate,
        var comment: String,
        var category: String,
        var isSaving: Boolean,
        var sequence: EntrySequence,
        var orderIndex: Int,
        var uuid: UUID = UUID.randomUUID()
) : Comparable<Entry> {

    constructor(amount: BigDecimal, entryDate: LocalDate, comment: String, category: String) :
            this(
                    amount,
                    entryDate,
                    comment,
                    category,
                    false,
                    EntrySequence.REGULAR,
                    0
            )

    private fun year() = entryDate.year
    private fun monthValue() = entryDate.monthValue
    private fun dayOfMonth() = entryDate.dayOfMonth

    override fun compareTo(other: Entry): Int {
        return compareValuesBy(
                this,
                other,
                compareByDescending(Entry::year)
                        .thenByDescending(Entry::monthValue)
                        .thenBy(Entry::sequence)
                        .thenBy(Entry::orderIndex)
                        .thenBy(Entry::dayOfMonth)
                        .thenBy(Entry::amount),
                { it })
    }

}