package de.omilke.bankingfx.report.categories.control

import de.omilke.bankingfx.controls.UIUtils
import de.omilke.bankingfx.report.categories.model.CategoryOverTimeElement
import de.omilke.bankingfx.report.control.CompareWithPreviousMonthPopoverHandler
import de.omilke.bankingfx.report.control.ShowEntryPopoverHandler
import javafx.event.EventHandler
import javafx.scene.control.ContextMenu
import javafx.scene.control.MenuItem
import javafx.scene.control.TableCell
import javafx.scene.input.MouseButton
import javafx.scene.input.MouseEvent
import java.math.BigDecimal
import java.time.YearMonth

/**
 * Created by Olli on 23.05.2017.
 */
class AmountContextMenuTableCell(private val periodStart: YearMonth? = null, private val periodEnd: YearMonth? = null) : TableCell<CategoryOverTimeElement?, BigDecimal?>() {

    init {
        onMouseClicked = EventHandler { setupContextMenu(it) }
    }

    private fun setupContextMenu(event: MouseEvent) {


        if (event.button == MouseButton.SECONDARY) {

            //lazily modify the context menu for this cell only on right click
            contextMenu = if (item != null && periodStart != null) {

                val selectedRow = this.tableRow.item
                val category = if (selectedRow!!.isAggregateRow) {
                    null
                } else {
                    selectedRow.categoryName
                }

                val parent = this

                val showEntriesItem = MenuItem("Show Entries").apply {
                    onAction = ShowEntryPopoverHandler(periodStart, parent, periodEnd, category)
                }

                val compareWithPreviousItem = MenuItem("Compare with previous month").apply {
                    onAction = CompareWithPreviousMonthPopoverHandler(periodStart, parent,  category)
                }

                ContextMenu(showEntriesItem, compareWithPreviousItem)
            } else {
                null
            }
        }

    }


    override fun updateItem(item: BigDecimal?, empty: Boolean) {

        super.updateItem(item, empty)

        UIUtils.formatAmount(this, item, empty)
    }

}