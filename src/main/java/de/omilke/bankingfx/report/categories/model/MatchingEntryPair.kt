package de.omilke.bankingfx.report.categories.model

import de.omilke.banking.account.entity.Entry

class MatchingEntryPair(val earlier: Entry?, val later: Entry?) : Comparable<MatchingEntryPair> {

    init {
        if (earlier == null && later == null) throw IllegalStateException("there must be at least one non-null pair.")
    }

    fun getReferenceEntry(): Entry {

        //the later entry is the more relevant entry
        //there always is at least one non-null value which taken care of in the constructor, which is why we can be sure here
        return later ?: earlier!!
    }

    fun isMatch(): Boolean {

        return earlier != null && later != null
    }

    fun dayOfMonthDiffers(): Boolean {

        return isMatch() &&
                earlier!!.entryDate.dayOfMonth != later!!.entryDate.dayOfMonth
    }

    fun amountDiffers(): Boolean {

        return isMatch() &&
                earlier!!.amount.compareTo(later!!.amount) != 0
    }

    fun categoryDiffers(): Boolean {

        return isMatch() &&
                earlier!!.category.compareTo(later!!.category) != 0
    }

    override fun compareTo(other: MatchingEntryPair): Int {

        val comparator =
                compareBy(::extractDayOfEntry)
                        .thenComparing(Entry::amount)
                        .thenComparing(Entry::comment)

        return comparator.compare(this.getReferenceEntry(), other.getReferenceEntry())
    }

    /**
     * Allow ordering by the day of Month in order to compare intra-month order
     */
    private fun extractDayOfEntry(entry: Entry): Int {
        return entry.entryDate.dayOfMonth
    }

}