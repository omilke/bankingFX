package de.omilke.bankingfx.main.entrylist.model;

import de.omilke.banking.account.entity.EntrySequence;

import java.time.LocalDate;

/**
 * Provides total order of entries with respect to {@link EntrySequence} and the order index of {@link Entry} entity.
 *
 * @author Oliver Milke
 * @since 10.08.2015
 */
public class EntryOrder implements Comparable<EntryOrder> {

    private final LocalDate date;
    private final EntrySequence sequence;
    private final int orderIndex;

    private final long order;

    private EntryOrder(final LocalDate date, final EntrySequence sequence, final int orderIndex) {

        this.date = date;
        this.sequence = sequence;
        this.orderIndex = orderIndex;

        this.order = calculateOrder(date, sequence, orderIndex);

    }

    public EntryOrder update(final EntrySequence newSequence, final int newOrderIndex) {

        return EntryOrder.of(date, newSequence, newOrderIndex);
    }

    public EntryOrder update(final LocalDate newDate) {

        return EntryOrder.of(newDate, sequence, orderIndex);
    }

    public final EntrySequence getSequence() {

        return sequence;
    }

    public final int getOrderIndex() {

        return orderIndex;
    }

    public final long getOrder() {

        return order;
    }

    @Override
    public final int compareTo(final EntryOrder o) {

        final long difference = this.order - o.order;

        final int result;
        if (difference < 0) {
            result = -1;
        } else if (difference > 0) {
            result = 1;
        } else {
            result = 0;
        }

        return result;
    }

    public static EntryOrder of(final LocalDate date, final EntrySequence sequence, final int orderIndex) {

        if (date == null || sequence == null) {
            return null;
        } else {
            return new EntryOrder(date, sequence, orderIndex);
        }
    }

    static long calculateOrder(final LocalDate date, final EntrySequence sequence, final int orderIndex) {

        // @formatter:off
        return date.getDayOfMonth()
                + orderIndex * 100L
                + sequence.ordinal() * (100L) * 100
                + date.getMonthValue() * (100L * 100) * 10
                + date.getYear() * (100L * 100 * 10) * 100;
        // @formatter:on

    }

}
