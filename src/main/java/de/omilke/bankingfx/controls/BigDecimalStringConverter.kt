package de.omilke.bankingfx.controls

import javafx.util.StringConverter
import java.math.BigDecimal
import java.text.DecimalFormat
import java.text.ParseException
import java.util.*

internal class BigDecimalStringConverter(private val locale: Locale) : StringConverter<BigDecimal?>() {
    //TODO: locale really necessary - or just read from configuredLocale()?


    //TODO: nullable necessary
    override fun toString(value: BigDecimal?): String {

        return UIUtils.formatAmount(value)
    }

    override fun fromString(string: String): BigDecimal? {

        return try {
            val df = DecimalFormat.getNumberInstance(locale) as DecimalFormat
            df.isParseBigDecimal = true
            df.parse(string) as BigDecimal
        } catch (e: ParseException) {
            null
        }
    }
}