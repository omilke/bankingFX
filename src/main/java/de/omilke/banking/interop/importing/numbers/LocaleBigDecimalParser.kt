package de.omilke.banking.interop.importing.numbers

import java.math.BigDecimal
import java.text.DecimalFormat
import java.text.NumberFormat
import java.text.ParseException
import java.util.*

/**
 * A parser for BigDecimal with locale specific parsing. If no locale is
 * specified, [Locale.GERMANY] is assumed.
 *
 * @author omilke <ollimi85></ollimi85>@gmail.com>
 */
class LocaleBigDecimalParser @JvmOverloads constructor(private val locale: Locale = Locale.GERMANY) {

    /**
     * Tries to parse the provided string value into a BigDecimal with respect
     * to the locale this instance has been created with.
     *
     * @param amount The expression to be parsed.
     * @return Returns the parsed BigDecimal, if possible.
     * @throws ParseException If the provided amount cannot be parsed with
     * respect to this instances locale.
     */
    @Throws(ParseException::class)
    fun parseBigDecimal(amount: String): BigDecimal {

        val df = NumberFormat.getInstance(locale) as DecimalFormat
        df.isParseBigDecimal = true

        return df.parseObject(amount) as BigDecimal
    }
}