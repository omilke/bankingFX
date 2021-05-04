package de.omilke.bankingfx.report.savings.model

import java.math.BigDecimal
import java.util.*

class Category(val name: String) {

    private var _sum = BigDecimal.ZERO

    val sum: BigDecimal
        get() = _sum

    val entries: SortedSet<Entry> = TreeSet(ENTRY_COMPARATOR)

    fun addEntry(entry: Entry) {

        entries.add(entry)
        _sum = _sum.add(entry.getAmount())
    }

    /**
     * Provides the lowercase name for building a case-insensitive comparator.
     */
    val lowerCaseName: String
        get() = name.toLowerCase()

    companion object {

        //TODO: replace with kotlin comparators?
        private val ENTRY_COMPARATOR =
                Comparator
                .comparing { obj: Entry -> obj.getEntryDate() }.reversed()
                .thenComparing { obj: Entry -> obj.getAmount() }
                .thenComparing { obj: Entry -> obj.getComment() }
    }
}