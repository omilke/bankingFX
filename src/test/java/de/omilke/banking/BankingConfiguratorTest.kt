@file:Suppress("JAVA_MODULE_DOES_NOT_DEPEND_ON_MODULE")

package de.omilke.banking

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class BankingConfiguratorTest {

    @Test
    internal fun testDefaultConfiguration() {

        assertThat(BankingConfigurator.configuredLocale()).isNotNull
    }
}

