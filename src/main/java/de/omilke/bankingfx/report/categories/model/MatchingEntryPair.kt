package de.omilke.bankingfx.report.categories.model

import de.omilke.banking.account.entity.Entry

class MatchingEntryPair(val earlier: Entry?, val later: Entry?) : Comparable<MatchingEntryPair> {

    init {
        if (earlier == null && later == null) throw IllegalStateException("there must be at least one non-null pair.")
    }

    fun getReferenceEntry(): Entry {

        return if (this.later != null) {
            //the later entry is the more relevant entry
            later
        } else {
            //there always is at least one non-null value which taken care of in the constructor, which is why we can be sure here
            earlier!!
        }
    }

    fun isMatch(): Boolean {

        return earlier != null && later != null
    }

    override fun compareTo(other: MatchingEntryPair): Int {

        val comparator = compareBy(this::extractDayOfEntry)
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