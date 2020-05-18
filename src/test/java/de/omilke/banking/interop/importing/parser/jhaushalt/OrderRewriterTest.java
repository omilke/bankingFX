package de.omilke.banking.interop.importing.parser.jhaushalt;


import de.omilke.banking.account.entity.EntrySequence;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class OrderRewriterTest {

    @Test
    public void testMapWithMatchFound() {

        OrderRewriter cut = new OrderRewriter(generateValidTestConfig());

        verifyMapping(cut, "Miete", 3, EntrySequence.FIRST);
        verifyMapping(cut, "Steuern", 11, EntrySequence.REGULAR);
        verifyMapping(cut, "Haftpflicht", 7, EntrySequence.FIRST);
    }

    @Test
    public void testMapWithInvalidConfig() {

        OrderRewriter cut = new OrderRewriter(generateInvalidTestConfig());

        assertThat(cut.getMappingCount()).isEqualTo(0);

    }

    @Test
    public void testMapWithoutMatch() {

        OrderRewriter cut = new OrderRewriter(new ArrayList<>());

        verifyMapping(cut, "any Value", 0, EntrySequence.REGULAR);

    }

    private void verifyMapping(OrderRewriter cut, String original, int expectedIndex, EntrySequence expectedSequence) {

        OrderRewriter.OrderSetting mapped = cut.map(original);

        assertThat(mapped.getEntrySequence()).isEqualTo(expectedSequence);
        assertThat(mapped.getOrderIndex()).isEqualTo(expectedIndex);
    }

    private List<String> generateValidTestConfig() {

        ArrayList<String> configItems = new ArrayList<>();

        configItems.add("Steuern;REGULAR;11");
        configItems.add("Haftpflicht;FIRST;7");
        configItems.add("randomValue;LAST;101");
        configItems.add("Miete;FIRST;3");

        return configItems;
    }

    private List<String> generateInvalidTestConfig() {

        ArrayList<String> configItems = new ArrayList<>();

        configItems.add("Steuern;FIRST;A");
        configItems.add("Steuern;ANY;11");
        configItems.add("Steuern;FIRST;A;");
        configItems.add("FIRST;A;");

        return configItems;
    }
}