package de.omilke.bankingfx.main.entrylist.model;

import de.omilke.banking.account.entity.EntrySequence;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Month;

import static org.assertj.core.api.Assertions.assertThat;

public class EntryOrderTest {

    @Test
    public void testEntryOrder() {

        assertThat(EntryOrder.calculateOrder(LocalDate.of(2015, Month.AUGUST, 10), EntrySequence.FIRST, 10)).isEqualTo(2015_08_0_10_10L);
        assertThat(EntryOrder.calculateOrder(LocalDate.of(2015, Month.AUGUST, 10), EntrySequence.REGULAR, 1)).isEqualTo(2015_08_1_01_10L);
        assertThat(EntryOrder.calculateOrder(LocalDate.of(2015, Month.AUGUST, 10), EntrySequence.LAST, 5)).isEqualTo(2015_08_2_05_10L);
    }

    @Test
    public void testUpdate() {

        final EntryOrder cut = EntryOrder.of(LocalDate.of(2015, Month.AUGUST, 15), EntrySequence.FIRST, 10);
        assertThat(cut.getOrder()).isEqualTo(2015_08_0_10_15L);

        EntryOrder updated = cut.update(EntrySequence.LAST, 11);
        assertThat(updated.getOrder()).isEqualTo(2015_08_2_11_15L);

        updated = cut.update(EntrySequence.REGULAR, 0);
        assertThat(updated.getOrder()).isEqualTo(2015_08_1_00_15L);
    }

    @Test
    public void testCompareTo() {

        // at the same day, the sequence decides the order
        EntryOrder first = EntryOrder.of(LocalDate.of(2015, Month.AUGUST, 10), EntrySequence.FIRST, 1);
        EntryOrder second = EntryOrder.of(LocalDate.of(2015, Month.AUGUST, 10), EntrySequence.REGULAR, 1);

        assertThat(first.compareTo(second)).isLessThan(0);
        assertThat(second.compareTo(first)).isGreaterThan(0);

        // at the same day and with the same sequence, the order index decides the order
        first = EntryOrder.of(LocalDate.of(2015, Month.AUGUST, 10), EntrySequence.FIRST, 1);
        second = EntryOrder.of(LocalDate.of(2015, Month.AUGUST, 10), EntrySequence.FIRST, 2);

        assertThat(first.compareTo(second)).isLessThan(0);
        assertThat(second.compareTo(first)).isGreaterThan(0);

        // within the same month, the sequence decides the order
        first = EntryOrder.of(LocalDate.of(2015, Month.AUGUST, 31), EntrySequence.FIRST, 99);
        second = EntryOrder.of(LocalDate.of(2015, Month.AUGUST, 1), EntrySequence.REGULAR, 1);

        assertThat(first.compareTo(second)).isLessThan(0);
        assertThat(second.compareTo(first)).isGreaterThan(0);

        // with the same sequence, the day decides the order
        first = EntryOrder.of(LocalDate.of(2015, Month.AUGUST, 1), EntrySequence.REGULAR, 1);
        second = EntryOrder.of(LocalDate.of(2015, Month.AUGUST, 2), EntrySequence.REGULAR, 1);

        assertThat(first.compareTo(second)).isLessThan(0);
        assertThat(second.compareTo(first)).isGreaterThan(0);

        // a higher month is always greater
        first = EntryOrder.of(LocalDate.of(2015, Month.AUGUST, 31), EntrySequence.REGULAR, 1);
        second = EntryOrder.of(LocalDate.of(2015, Month.SEPTEMBER, 1), EntrySequence.REGULAR, 1);

        assertThat(first.compareTo(second)).isLessThan(0);
        assertThat(second.compareTo(first)).isGreaterThan(0);

        // a higher month is always greater, no matter the sequence or the order index
        first = EntryOrder.of(LocalDate.of(2015, Month.AUGUST, 31), EntrySequence.LAST, 1);
        second = EntryOrder.of(LocalDate.of(2015, Month.SEPTEMBER, 1), EntrySequence.FIRST, 99);

        assertThat(first.compareTo(second)).isLessThan(0);
        assertThat(second.compareTo(first)).isGreaterThan(0);
    }

}
