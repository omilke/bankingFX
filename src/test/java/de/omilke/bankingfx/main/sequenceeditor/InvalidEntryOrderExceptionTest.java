package de.omilke.bankingfx.main.sequenceeditor;

import org.junit.jupiter.api.Test;

import static de.omilke.bankingfx.main.sequenceeditor.InvalidEntryOrderExceptionKt.formatMessage;
import static org.assertj.core.api.Assertions.assertThat;

public class InvalidEntryOrderExceptionTest {

    @Test
    public void testFormatMessage() {

        String formatMessage = formatMessage("abc");
        assertThat(formatMessage).contains("abc");

        formatMessage = formatMessage(-1);
        assertThat(formatMessage).contains("-1");
    }

}
