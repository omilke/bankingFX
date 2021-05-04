package de.omilke.bankingfx.report.categories.control

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon
import de.omilke.banking.account.entity.Entry
import de.omilke.bankingfx.UIConstants
import de.omilke.bankingfx.controls.UIUtils.getIconWithColor
import javafx.scene.control.TableCell
import javafx.scene.paint.Color
import java.math.BigDecimal

/**
 * Created by Olli on 27.05.2017.
 */
class SavingTableCell : TableCell<Entry?, Boolean?>() {

    override fun updateItem(item: Boolean?, empty: Boolean) {

        super.updateItem(item, empty)

        val rowItem: Any? = tableRow.item
        graphic = when {
            !empty && item != null && item && rowItem != null -> {

                val (amount) = rowItem as Entry
                styleClass.add(UIConstants.ALIGN_CENTER)
                when {
                    amount < BigDecimal.ZERO -> getIconWithColor(FontAwesomeIcon.DOWNLOAD, Color.GREEN)
                    else -> getIconWithColor(FontAwesomeIcon.UPLOAD, Color.RED)
                }
            }
            else -> null
        }
    }
}