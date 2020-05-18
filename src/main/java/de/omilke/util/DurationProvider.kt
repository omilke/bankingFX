package de.omilke.util

import java.text.NumberFormat
import java.util.*

object DurationProvider {


    fun formatDurationSince(since: Long, locale: Locale = Locale.getDefault()): String {

        return formatDuration(since, System.nanoTime(), locale)
    }

    fun formatDuration(since: Long, to: Long, locale: Locale = Locale.getDefault()): String {

        val numberFormat = NumberFormat.getNumberInstance(locale)

        val delta = (to - since) / 1_000_000.0
        return if (delta > 1000) {
            numberFormat.format(delta / 1_000.0).plus("s")
        } else {
            numberFormat.format(delta).plus("ms")
        }
    }
}