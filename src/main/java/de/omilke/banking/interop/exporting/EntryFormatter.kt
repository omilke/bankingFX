package de.omilke.banking.interop.exporting;

import de.omilke.banking.account.entity.Entry;

public interface EntryFormatter {

    /**
     * Provides a flat representation of the Entry.
     *
     * @param entry The entry to be formatted.
     */
    String format(Entry entry);

}
