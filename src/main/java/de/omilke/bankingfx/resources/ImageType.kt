package de.omilke.bankingfx.resources;

/**
 * Created by olli on 27.11.2015.
 */
public enum ImageType {

    MAIN("book.png"),

    CHART("bar-chart.png");

    private final String value;

    ImageType(String value) {

        this.value = value;
    }

    public String getValue() {

        return value;
    }
}
