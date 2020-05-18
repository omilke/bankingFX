package de.omilke.bankingfx.report.control

import de.omilke.bankingfx.controls.EntrylistPopover
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.scene.Node
import java.time.YearMonth

/**
 * This Action-EventHandler lazily constructs an EntrylistPopover for the given period and displays it.
 */
class ShowEntryPopoverHandler(private val period: YearMonth, private val node: Node, private val periodEnd: YearMonth? = null, private val category: String? = null) : EventHandler<ActionEvent> {

    override fun handle(event: ActionEvent) {

        EntrylistPopover(period, periodEnd, category).show(node)
    }
}