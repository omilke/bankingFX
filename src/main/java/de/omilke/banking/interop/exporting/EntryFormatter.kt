package de.omilke.banking.interop.exporting

import de.omilke.banking.account.entity.Entry

interface EntryFormatter {

    /**
     * Provides a flat representation of the Entry.
     *
     * @param entry The entry to be formatted.
     */
    fun format(entry: Entry): String
}