package de.omilke.banking.interop.exporting.jhaushalt;

import de.omilke.banking.account.entity.Entry;
import de.omilke.banking.account.entity.EntrySequence;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Olli on 13.04.2017.
 */
public class JHaushaltFormatterTest {

    private static final String EXPECTED_BASIC = "\"01.05.01\";\"Miete & Nebenkosten\";\"exported\";\"-10,50 ¬\";\"[Giro]\"";
    private static final String EXPECTED_WITH_ACTUAL_CATEGORY = "\"01.05.01\";\"Miete & Nebenkosten\";\"fixe Kosten\";\"-10,50 ¬\";\"[Giro]\"";
    private static final String EXPECTED_WITH_EMPTY = "\"01.05.02\";\"\";\"exported\";\"0,00 ¬\";\"[Giro]\"";
    private static final String EXPECTED_WITH_LARGE_NUMBER = "\"13.04.00\";\"large amount\";\"exported\";\"1000000,00 ¬\";\"[Giro]\"";

    @Test
    public void basicEntryCanBeFormatted() {

        JHaushaltFormatter cut = new JHaushaltFormatter();

        String comment = "Miete & Nebenkosten";
        BigDecimal amount = new BigDecimal("-10.5");
        LocalDate date = LocalDate.of(2001, Month.MAY, 1);

        Entry entry = new Entry(amount, date, comment, "exported");
        String format = cut.format(entry);

        assertThat(format).isEqualTo(EXPECTED_BASIC);
    }

    @Test
    public void emptyEntryCanBeFormatted() {

        JHaushaltFormatter cut = new JHaushaltFormatter();

        LocalDate entryDate = LocalDate.of(2002, Month.MAY, 1);
        Entry entry = new Entry(BigDecimal.ZERO, entryDate, "", "exported");

        String format = cut.format(entry);

        assertThat(format).isEqualTo(EXPECTED_WITH_EMPTY);
    }

    @Test
    public void entryWithLargeNumberCanBeFormatted() {

        JHaushaltFormatter cut = new JHaushaltFormatter();

        String comment = "large amount";
        BigDecimal amount = (new BigDecimal(1_000_000));
        LocalDate date = (LocalDate.of(2000, Month.APRIL, 13));
        Entry entry = new Entry(amount, date, comment, "exported");

        String format = cut.format(entry);

        assertThat(format).isEqualTo(EXPECTED_WITH_LARGE_NUMBER);
    }

    @Test
    public void entryCanBeFormattedWithActualCategoryName() {

        JHaushaltFormatter cut = new JHaushaltFormatter(true);

        String comment = "Miete & Nebenkosten";
        BigDecimal amount = new BigDecimal("-10.5");
        LocalDate date = LocalDate.of(2001, Month.MAY, 1);

        Entry entry = new Entry(amount, date, comment, "fixe Kosten", false, EntrySequence.REGULAR, 0, UUID.randomUUID());
        String format = cut.format(entry);

        assertThat(format).isEqualTo(EXPECTED_WITH_ACTUAL_CATEGORY);

    }
}