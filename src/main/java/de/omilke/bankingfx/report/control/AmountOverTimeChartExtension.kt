package de.omilke.bankingfx.report.control

import de.omilke.bankingfx.UIConstants
import javafx.collections.FXCollections
import javafx.scene.chart.CategoryAxis
import javafx.scene.chart.XYChart
import javafx.scene.control.Tooltip
import java.math.BigDecimal
import java.time.YearMonth

fun XYChart<String, BigDecimal>.setChartValues(
        dataSource: Map<YearMonth, BigDecimal>,
        formatDate: (YearMonth) -> String) {

    val series = XYChart.Series<String, BigDecimal>()
    val categories = FXCollections.observableArrayList<String>()

    for ((key, value) in dataSource) {

        val category = formatDate(key)
        categories.add(category)

        val element = XYChart.Data(category, value)
        series.data.add(element)
    }

    this.data.add(series)
    (this.xAxis as CategoryAxis).categories = categories
}


fun XYChart<String, BigDecimal>.styleNegativeValuesWith(styleClass: String) {

    for (series in this.data) {
        for (data in series.data) {

            if (data.yValue < BigDecimal.ZERO) {
                data.node.styleClass.add(styleClass)
            }

        }
    }
}

fun XYChart<String, BigDecimal>.setToolTips(buildTooltipText: (String, BigDecimal) -> String) {

    for (series in this.data) {
        for (data in series.data) {

            val tooltip = Tooltip(buildTooltipText(data.xValue, data.yValue)).apply {
                styleClass.add(UIConstants.CHART_TOOLTIP)
            }

            Tooltip.install(data.node, tooltip)
        }
    }
}

fun XYChart<String, BigDecimal>.setContextMenu(resolveDate: (String) -> YearMonth) {

    for (series in this.data) {
        for (data in series.data) {

            data.node.onMouseClicked = ChartShowEntriesMouseEventHandler(data, resolveDate)
        }
    }
}

fun XYChart<String, BigDecimal>.styleAxes() {

    this.xAxis.tickLabelRotation = 45.0
    this.yAxis.label = "[€]"
}


