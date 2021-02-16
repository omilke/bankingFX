package de.omilke.bankingfx.report.control

import de.omilke.bankingfx.controls.CompareWithPreviousMonthPopover
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.scene.Node
import java.time.YearMonth

/**
 * This Action-EventHandler lazily constructs an CompareWithPreviousMonthPopoverHandler for the given period and displays it.
 */
class CompareWithPreviousMonthPopoverHandler(private val period: YearMonth, private val node: Node, private val category: String? = null) : EventHandler<ActionEvent> {

    override fun handle(event: ActionEvent) {

        CompareWithPreviousMonthPopover(period, category).show(node)
    }
}