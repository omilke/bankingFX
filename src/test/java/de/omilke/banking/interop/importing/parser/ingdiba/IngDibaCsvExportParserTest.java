package de.omilke.banking.interop.importing.parser.ingdiba;

import de.omilke.banking.account.entity.Entry;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class IngDibaCsvExportParserTest {

    private static final String REFERENCE_0 = "18.06.2018;18.06.2018;X Fitness Corp;Lastschrift;X Vertrag 18.06.18 33,90;1.111,11;EUR;-33,90;EUR";
    private static final String REFERENCE_1 = "29.03.2018;30.03.2018;;Abschluss;;0,00;EUR;-0,37;EUR";
    private static final String REFERENCE_2 = "29.03.2018;;0,00;EUR;-0,37;EUR";

    @Test
    public void regularStringCanBeParsed() {

        IngDibaCsvExportParser cut = new IngDibaCsvExportParser();

        Entry extractEntry = cut.extractEntry(REFERENCE_0).get();

        assertThat(extractEntry).isNotNull();
        assertThat(extractEntry.getCategory()).isEqualTo("imported");

        assertThat(extractEntry.getAmount()).isEqualByComparingTo(new BigDecimal("-33.90"));

        //should be "Buchung"
        assertThat(extractEntry.getEntryDate()).isEqualTo(LocalDate.of(2018, Month.JUNE, 18));

        //make sure both receiver and text are trimmed independently and then joined
        assertThat(extractEntry.getComment()).isEqualTo("X Fitness Corp - X Vertrag 18.06.18 33,90");
    }

    @Test
    public void withoutReceiverCanBeParsed() {

        IngDibaCsvExportParser cut = new IngDibaCsvExportParser();

        Entry extractEntry = cut.extractEntry(REFERENCE_1).get();

        assertThat(extractEntry).isNotNull();
        assertThat(extractEntry.getCategory()).isEqualTo("imported");

        assertThat(extractEntry.getAmount()).isEqualByComparingTo(new BigDecimal("-0.37"));
        assertThat(extractEntry.getEntryDate()).isEqualTo(LocalDate.of(2018, Month.MARCH, 29));
        assertThat(extractEntry.getComment()).isEqualTo("Abschluss");
    }

    @Test
    public void invalidLines() {

        IngDibaCsvExportParser cut = new IngDibaCsvExportParser();

        Optional<Entry> result = cut.extractEntry("");

        assertThat(result.isPresent()).isFalse();

        result = cut.extractEntry(REFERENCE_2);
        assertThat(result.isPresent()).isFalse();

    }

}