package de.omilke.bankingfx.recurringentries.model

import de.omilke.banking.account.entity.Entry
import de.omilke.bankingfx.main.entrylist.model.EntryOrder

val Entry.entryOrder: EntryOrder
    get() {
        return EntryOrder.of(entryDate, sequence, orderIndex)
    }
