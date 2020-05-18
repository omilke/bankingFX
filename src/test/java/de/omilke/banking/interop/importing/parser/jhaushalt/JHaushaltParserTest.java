package de.omilke.banking.interop.importing.parser.jhaushalt;

import de.omilke.banking.account.entity.Entry;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * @author olli
 */
public class JHaushaltParserTest {

    @Test
    public void testExtractEntry() {

        final String exampleLine = "\"14.08.09\";\"Geschäft\";\"Einkaufen\";\"-3,44 ¬\";\"[Giro]\"";

        final JHaushaltParser parser = new JHaushaltParser(null, null);

        final Optional<Entry> result = parser.extractEntry(exampleLine);

        assertThat(result).isNotEmpty();
        assertThat(result.get().getCategory()).isEqualTo("Einkaufen");
        assertThat(result.get().getAmount()).isEqualByComparingTo(new BigDecimal("-3.44"));
        assertThat(result.get().getEntryDate()).isEqualTo(LocalDate.of(2009, 8, 14));
        assertThat(result.get().getComment()).isEqualTo("Geschäft");
    }

    @Test
    public void testExtractEntryFromInvalidLine() {

        final JHaushaltParser parser = new JHaushaltParser(null, null);


        Optional<Entry> result = parser.extractEntry("");
        assertThat(result).isEmpty();

        final String exampleLine = "\"14.08.09\";\"Geschäft\";\"Einkaufen\";\"[Giro]\"";
        result = parser.extractEntry(exampleLine);

        assertThat(result).isEmpty();
    }
}
