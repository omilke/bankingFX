package de.omilke.bankingfx.report.control

import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.scene.control.ContextMenu
import javafx.scene.control.MenuItem

class ShowEntryContextMenu(private val actionHandler: EventHandler<ActionEvent>) : ContextMenu() {

    init {

        val menuItem = MenuItem("Show Entries").apply {
            onAction = actionHandler
        }

        items.add(menuItem)
    }
}