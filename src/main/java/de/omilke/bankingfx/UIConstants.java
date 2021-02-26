package de.omilke.bankingfx;

import java.time.format.DateTimeFormatter;

public class UIConstants {

    public static final double AMOUNT_WIDTH = 100;
    public static final double CATEGORY_WIDTH = 250;
    public static final double CATEGORY_COMPACT_WIDTH = 200;
    public static final double COMMENT_WIDTH = 400;
    public static final double COMMENT_COMPACT_WIDTH = 225;
    public static final double COMMENT_IMPORT_WIDTH = 475;
    public static final double ACTION_IMPORT_WIDTH = 100;
    public static final double DATE_WIDTH = 100;
    public static final double RECURRENCE_DATE_WIDTH = 150;
    public static final double RECURRENCE_WIDTH = 100;
    public static final double MONTH_WIDTH = 150;
    public static final double SAVING_WIDTH = 75;
    public static final double SEQUENCE_WIDTH = 50;

    public static final String POSITIVE = "positive";
    public static final String NEGATIVE = "negative";

    public static final String ALIGN_LEFT = "left";
    public static final String ALIGN_CENTER = "center";
    public static final String ALIGN_RIGHT = "right";

    public static final String CHART_TOOLTIP = "chart-tooltip";

    public static final DateTimeFormatter MONTH_FORMATTER = DateTimeFormatter.ofPattern("MM / yyyy");
    public static final DateTimeFormatter MONTH_NAME_FORMATTER = DateTimeFormatter.ofPattern("MMMM yyyy");
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    public static String getCssUri() {
        return UIConstants.class.getClassLoader().getResource("compiled-css/bankingfx.css").toExternalForm();
    }

}
