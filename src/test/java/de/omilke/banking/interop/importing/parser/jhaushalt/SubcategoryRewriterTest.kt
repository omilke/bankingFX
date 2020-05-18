@file:Suppress("JAVA_MODULE_DOES_NOT_DEPEND_ON_MODULE")

package de.omilke.banking.interop.importing.parser.jhaushalt

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class SubcategoryRewriterTest {

    @Test
    fun testMapping() {

        val items = listOf("Verliehen;P1;P1", "Rücklage;Nebenkosten 2000;Nebenkosten 2000", "Rücklage;Nebenkosten;Nebenkosten 2001")
        val cut = SubcategoryRewriter(items)

        assertThat(cut.mappingCount).isEqualTo(3)

        //only trigger if orignal category is actually present
        assertThat(cut.map("SomeOther", "X für P1")).isEqualTo("SomeOther")
        assertThat(cut.map("Verliehen", "X für P1")).isEqualTo("Verliehen:P1")

        //respect kewords in the order of declaration (important in case of duplication in (parts of) keywords)
        assertThat(cut.map("Rücklage", "die Nebenkosten 2000")).isEqualTo("Rücklage:Nebenkosten 2000")
        assertThat(cut.map("Rücklage", "die Nebenkosten von damals...")).isEqualTo("Rücklage:Nebenkosten 2001")
    }

}