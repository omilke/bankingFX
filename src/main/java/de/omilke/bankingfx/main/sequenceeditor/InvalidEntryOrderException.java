package de.omilke.bankingfx.main.sequenceeditor;

public class InvalidEntryOrderException extends Exception {

    private static final long serialVersionUID = 1L;

    public InvalidEntryOrderException(final int orderIndex) {

        super(formatMessage(orderIndex));
    }

    public InvalidEntryOrderException(final String orderIndex) {

        super(formatMessage(orderIndex));
    }

    static String formatMessage(final Object orderIndex) {

        return String.format("Invalid Order Index: %s", orderIndex);
    }

}
