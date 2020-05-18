package de.omilke.bankingfx.recurringentries.control

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon
import de.omilke.banking.account.entity.RecurringEntry
import de.omilke.bankingfx.UIConstants
import de.omilke.bankingfx.controls.UIUtils
import javafx.scene.control.TableCell
import javafx.scene.paint.Color
import java.math.BigDecimal

//TODO unify with all the SavingTableCells... it's essentially always the same

//TODO maybe even make EntryTableView base class?
class SavingTableCell : TableCell<RecurringEntry, Boolean>() {

    override fun updateItem(item: Boolean?, empty: Boolean) {

        super.updateItem(item, empty)

        styleClass.add(UIConstants.ALIGN_CENTER)

        val rowItem: RecurringEntry? = tableRow.item
        graphic = if (!empty && item != null && item && rowItem != null) {

            when {
                rowItem.amount < BigDecimal.ZERO -> UIUtils.getIconWithColor(FontAwesomeIcon.DOWNLOAD, Color.GREEN)
                else -> UIUtils.getIconWithColor(FontAwesomeIcon.UPLOAD, Color.RED)
            }
        } else {
            null
        }

    }
}