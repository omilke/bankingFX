package de.omilke.bankingfx.main.entrylist.model

import de.omilke.banking.account.entity.Entry

fun Entry.buildEntryOrder(): EntryOrder {

    return EntryOrder(this.entryDate, this.sequence, this.orderIndex)

}