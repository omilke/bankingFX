package de.omilke.banking.account.entity;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class EntrySequenceTest {

    @Test
    public void toEntrySequenceValid() {

        EntrySequence sequence = EntrySequence.Companion.of(EntrySequence.REGULAR.toString());
        assertThat(sequence).isEqualTo(EntrySequence.REGULAR);
    }

    @Test
    public void toEntrySequenceFromOrdinalValid() {

        EntrySequence sequence = EntrySequence.Companion.of(EntrySequence.REGULAR.ordinal());
        assertThat(sequence).isEqualTo(EntrySequence.REGULAR);
    }

    @Test
    public void toEntrySequenceInvalid() {

        EntrySequence sequence = EntrySequence.Companion.of("any");
        assertThat(sequence).isNull();

        sequence = EntrySequence.Companion.of("");
        assertThat(sequence).isNull();

        sequence = EntrySequence.Companion.of((String) null);
        assertThat(sequence).isNull();

    }

}