package de.omilke.bankingfx.main.entrylist.model;

import de.omilke.banking.account.entity.EntrySequence;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;

import static org.assertj.core.api.Assertions.assertThat;

public class EntryTest {

    @Test
    public void testUpdateEntryDate() {

        final Entry cut = new Entry(LocalDate.of(2015, Month.AUGUST, 24), EntrySequence.REGULAR, 0, BigDecimal.ONE, false, "category", "comment");

        assertThat(cut.getEntryOrder().getOrder()).isEqualTo(2015_08_1_00_24L);

        cut.setEntryDate(LocalDate.of(2015, Month.SEPTEMBER, 1));
        assertThat(cut.getEntryOrder().getOrder()).isEqualTo(2015_09_1_00_01L);
    }

}
