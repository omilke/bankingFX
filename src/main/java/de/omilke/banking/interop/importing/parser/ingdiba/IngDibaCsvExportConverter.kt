package de.omilke.banking.interop.importing.parser.ingdiba

import de.omilke.banking.account.entity.Entry
import de.omilke.banking.interop.importing.parser.EntryParser
import java.util.*

class IngDibaCsvExportConverter {

    private val parser: EntryParser = IngDibaCsvExportParser()

    fun convert(lines: List<String>): List<Entry> {

        val result = ArrayList<Entry>()
        for (current in lines) {

            val parserResult = parser.extractEntry(current)
            if (parserResult.isPresent) {

                result.add(parserResult.get())
            }
        }

        return result
    }
}