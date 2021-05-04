package de.omilke.bankingfx.report.savings.model;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;

public class Category {

    private final String name;

    private BigDecimal sum = BigDecimal.ZERO;
    private final SortedSet<Entry> entries = new TreeSet<>(ENTRY_COMPARATOR);

    public Category(String name) {
        this.name = name;
    }

    public void addEntry(Entry entry) {

        entries.add(entry);
        this.sum = this.sum.add(entry.getAmount());
    }

    public String getName() {
        return name;
    }

    public SortedSet<Entry> getEntries() {
        return entries;
    }

    public BigDecimal getSum() {
        return sum;
    }

    /**
     * Provides the lowercase name for building a case-insensitive comparator.
     */
    public String getLowerCaseName() {
        return this.name.toLowerCase();
    }

    private static final Comparator<Entry> ENTRY_COMPARATOR = Comparator
            .comparing(Entry::getEntryDate).reversed()
            .thenComparing(Entry::getAmount)
            .thenComparing(Entry::getComment);

}
