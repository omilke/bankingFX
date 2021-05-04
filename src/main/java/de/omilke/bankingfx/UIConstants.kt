package de.omilke.bankingfx

import java.time.format.DateTimeFormatter

object UIConstants {

    const val AMOUNT_WIDTH = 100.0
    const val CATEGORY_WIDTH = 250.0
    const val CATEGORY_COMPACT_WIDTH = 200.0
    const val COMMENT_WIDTH = 400.0
    const val COMMENT_COMPACT_WIDTH = 225.0
    const val COMMENT_IMPORT_WIDTH = 475.0
    const val ACTION_IMPORT_WIDTH = 100.0
    const val DATE_WIDTH = 100.0
    const val RECURRENCE_DATE_WIDTH = 150.0
    const val RECURRENCE_WIDTH = 100.0
    const val MONTH_WIDTH = 150.0
    const val SAVING_WIDTH = 75.0
    const val SEQUENCE_WIDTH = 50.0
    const val SECURITY_NAME_WIDTH = 350.0
    const val TRANSACTION_COUNT_WIDTH = 75.0

    const val POSITIVE = "positive"
    const val NEGATIVE = "negative"
    const val ALIGN_LEFT = "left"
    const val ALIGN_CENTER = "center"
    const val ALIGN_RIGHT = "right"

    const val CHART_TOOLTIP = "chart-tooltip"

    const val DESCRIPTION_COLUMN: String = "description"
    const val GROUP_ROW = "group-row"
    const val AGGREGATE_ROW = "aggregate-row"

    val MONTH_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("MM / yyyy")
    val MONTH_NAME_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("MMMM yyyy")
    val MONTH_NAME_FORMATTER_FILENAME: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMM")
    val DATE_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")

    @Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    val cssUri: String
        get() = UIConstants::class.java.classLoader.getResource("compiled-css/bankingfx.css").toExternalForm()
}