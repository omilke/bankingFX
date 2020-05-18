package de.omilke.bankingfx.report.control

import javafx.event.EventHandler
import javafx.scene.chart.XYChart
import javafx.scene.input.MouseButton
import javafx.scene.input.MouseEvent
import java.math.BigDecimal
import java.time.YearMonth

/**
 * This EventHandler for mouse clicks lazily parses the clicked category / data point (e. g. bar / dot) to decide for
 * which month to show the entries and lazily constructs the context menu.
 *
 * Lazy in that case means only once the data element is right-clicked.
 */
class ChartShowEntriesMouseEventHandler(private val data: XYChart.Data<String, BigDecimal>, private val resolveDate: (String) -> YearMonth) : EventHandler<MouseEvent> {

    private val menu by lazy {

        val period = resolveDate(data.xValue)

        val actionHandler = ShowEntryPopoverHandler(period, data.node)
        ShowEntryContextMenu(actionHandler)
    }

    override fun handle(event: MouseEvent) {

        if (event.button == MouseButton.SECONDARY) {
            menu.show(data.node, event.screenX, event.screenY)
        }
    }
}