package de.omilke.bankingfx.report.savings.model

import java.math.BigDecimal
import java.util.*

class Category(val name: String) : Comparable<Category> {

    private var _sum = BigDecimal.ZERO

    val sum: BigDecimal
        get() = _sum

    val entries: SortedSet<Entry> = TreeSet(SavingEntryComparator())

    fun addEntry(entry: Entry) {

        entries.add(entry)
        _sum = _sum.add(entry.getAmount())
    }

    /**
     * Provides the lowercase name for building a case-insensitive comparator.
     */
    private val lowerCaseName: String
        get() = name.toLowerCase()

    override fun compareTo(other: Category): Int {
        return compareValuesBy(
                this,
                other,
                compareBy(Category::sum).thenComparing(Category::lowerCaseName),
                { it })
    }
}