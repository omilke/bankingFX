package de.omilke.bankingfx.controls;

import javafx.util.StringConverter;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

class BigDecimalStringConverter extends StringConverter<BigDecimal> {

    private final Locale locale;

    public BigDecimalStringConverter(final Locale locale) {

        this.locale = locale;
    }

    @Override
    public String toString(final BigDecimal object) {

        return NumberFormat.getCurrencyInstance(locale).format(object);
    }

    @Override
    public BigDecimal fromString(final String string) {

        try {
            final DecimalFormat df = (DecimalFormat) DecimalFormat.getNumberInstance(locale);
            df.setParseBigDecimal(true);

            return (BigDecimal) df.parse(string);
        } catch (final ParseException e) {
            return null;
        }
    }

}