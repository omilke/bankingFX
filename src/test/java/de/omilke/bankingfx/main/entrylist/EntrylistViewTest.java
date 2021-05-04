package de.omilke.bankingfx.main.entrylist;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

public class EntrylistViewTest {

    @Test
    public void testIsInFutureMonth() {

        final EntrylistView cut = new EntrylistView();

        assertThat(cut.isInFutureMonth(LocalDate.now().plusMonths(-1))).isFalse();
        assertThat(cut.isInFutureMonth(LocalDate.now())).isFalse();

        assertThat(cut.isInFutureMonth(LocalDate.now().plusMonths(1))).isTrue();
        assertThat(cut.isInFutureMonth(LocalDate.now().plusMonths(2))).isTrue();
    }

}
