package de.omilke.bankingfx.report.audit;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;

public class AuditViewTest {

    @Test
    public void testValueOf() {

        final AuditView cut = new AuditView();
        cut.locale = Locale.GERMANY;

        assertThat(cut.valueOf(null)).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(cut.valueOf("")).isEqualByComparingTo(BigDecimal.ZERO);

        assertThat(cut.valueOf("123")).isEqualByComparingTo(new BigDecimal(123));
        assertThat(cut.valueOf("123,456")).isEqualByComparingTo(new BigDecimal("123.456"));

        assertThat(cut.valueOf("123 €")).isEqualByComparingTo(new BigDecimal(123));
        assertThat(cut.valueOf("123,456 €")).isEqualByComparingTo(new BigDecimal("123.456"));
    }

    @Test
    public void testCalculateDelta() {

        final AuditView cut = new AuditView();

        BigDecimal result = cut.calculateDelta(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO);

        assertThat(result).isEqualByComparingTo(BigDecimal.ZERO);

        //we have TEN, with ZERO being planned --> TEN more than expected
        result = cut.calculateDelta(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.TEN, BigDecimal.ZERO);
        assertThat(result).isEqualByComparingTo(BigDecimal.TEN);

        //the other way around
        result = cut.calculateDelta(BigDecimal.TEN, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO);
        assertThat(result).isEqualByComparingTo(new BigDecimal(-10));

        //we have 13 with 12 being planned and additionally 2 saved --> we should have 1 more
        result = cut.calculateDelta(new BigDecimal(12), new BigDecimal(2), new BigDecimal(13), BigDecimal.ZERO);
        assertThat(result).isEqualByComparingTo(new BigDecimal(-1));

        //we have 13 (1 from planned not yet reflected on bankaccount) with 12 being planned and additionally 2 saved --> we have as much as was planned
        result = cut.calculateDelta(new BigDecimal(12), new BigDecimal(2), new BigDecimal(13), new BigDecimal(1));
        assertThat(result).isEqualByComparingTo(new BigDecimal(0));

        //we have 13 (-1 from planned not yet reflected on bankaccount) with 12 being planned and additionally savings overdue by 2 saved --> we have 2 more than as planned
        result = cut.calculateDelta(new BigDecimal(12), new BigDecimal(-2), new BigDecimal(13), new BigDecimal(-1));
        assertThat(result).isEqualByComparingTo(new BigDecimal(2));
    }

}
