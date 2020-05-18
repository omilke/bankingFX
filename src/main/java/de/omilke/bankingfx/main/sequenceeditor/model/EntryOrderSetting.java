package de.omilke.bankingfx.main.sequenceeditor.model;

import de.omilke.banking.account.entity.EntrySequence;

public class EntryOrderSetting {

    private final EntrySequence entrySequence;

    private final int orderIndex;

    public EntryOrderSetting(final EntrySequence entrySequence, final int orderIndex) {

        this.entrySequence = entrySequence;
        this.orderIndex = orderIndex;
    }

    public EntrySequence getEntrySequence() {

        return entrySequence;
    }

    public int getOrderIndex() {

        return orderIndex;
    }

}
