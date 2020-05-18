package de.omilke.bankingfx.report.categories.control

import javafx.util.StringConverter

/**
 * allows rendering a category in the combobox and most importantly, defines a representation for an empty filter.
 */
class CategoryConverter : StringConverter<String>() {

    override fun toString(any: String?): String {

        return any ?: ALL
    }

    override fun fromString(string: String): String? {

        return if (string == ALL) null
        else string

    }

    companion object {

        const val ALL = "- all -"
    }
}
