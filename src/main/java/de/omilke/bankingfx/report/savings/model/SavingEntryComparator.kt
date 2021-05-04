package de.omilke.bankingfx.report.savings.model

class SavingEntryComparator : Comparator<Entry> {

    override fun compare(o1: Entry?, o2: Entry?): Int {

        return compareValuesBy(o1,
                o2,
                compareByDescending(Entry::getEntryDate)
                        .thenComparing(Entry::getAmount)
                        .thenComparing(Entry::getComment),
                { it }
        )
    }
}