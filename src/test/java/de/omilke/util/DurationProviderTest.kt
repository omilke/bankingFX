@file:Suppress("JAVA_MODULE_DOES_NOT_DEPEND_ON_MODULE")

package de.omilke.util

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.util.*

internal class DurationProviderTest {

    @Test
    fun testFormatDurationWithGermanLocale() {

        val germany = Locale.GERMANY

        assertThat(DurationProvider.formatDuration(0, 1_000L, germany)).isEqualTo("0,001ms")
        assertThat(DurationProvider.formatDuration(0, 1_000_000L, germany)).isEqualTo("1ms")
        assertThat(DurationProvider.formatDuration(0, 1_000_000_000L, germany)).isEqualTo("1.000ms")
        assertThat(DurationProvider.formatDuration(0, 1_100_000_000L, germany)).isEqualTo("1,1s")
    }

    @Test
    internal fun testDurationSince() {

        val since = System.nanoTime() - 1_000_000

        //only sure it formats to {m}s
        assertThat(DurationProvider.formatDurationSince(since)).endsWith("s")
    }
}