@file:Suppress("JAVA_MODULE_DOES_NOT_DEPEND_ON_MODULE")

package de.omilke.bankingfx.report.categories.control

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

/**
 * Created by Olli on 05.06.2017.
 */
class CategoryConverterTest {

    @Test
    fun testConversion() {

        val cut = CategoryConverter()

        assertThat(cut.toString(null)).isEqualTo(CategoryConverter.ALL)
        assertThat(cut.toString("Any:Category")).isEqualTo("Any:Category")

        assertThat(cut.fromString(CategoryConverter.ALL)).isNull()
        assertThat(cut.fromString("Any:Category")).isEqualTo("Any:Category")

    }

}