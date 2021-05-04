package de.omilke.bankingfx.controls

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon
import de.omilke.bankingfx.controls.UIUtils.getIconWithColor
import de.omilke.bankingfx.main.entrylist.model.EntryTableRow
import javafx.event.EventHandler
import javafx.scene.control.CheckBox
import javafx.scene.control.TableCell
import javafx.scene.paint.Color
import java.math.BigDecimal

class SavingEditingCell<P> : TableCell<P, Boolean?>() {

    private val checkBox: CheckBox

    init {
        text = null

        checkBox = CheckBox().apply {
            this.onAction = EventHandler { commitEdit(this.selectedProperty().value) }
        }
    }

    override fun startEdit() {

        if (!isEmpty) {
            super.startEdit()

            displayEditControl(item!!)

            checkBox.requestFocus()
        }
    }

    override fun cancelEdit() {

        super.cancelEdit()

        displayValue(item)
    }

    override fun updateItem(item: Boolean?, empty: Boolean) {

        super.updateItem(item, empty)

        if (empty || item == null) {
            setGraphic(null)
        } else {

            if (isEditing) {
                displayEditControl(item)
            } else {
                displayValue(item)
            }
        }
    }

    private fun displayValue(item: Boolean?) {

        graphic = when (item) {
            true -> {

                // this is a saving -> define the direction of the saving
                val amount = (tableRow.item as EntryTableRow).getAmount()
                if (amount!! < BigDecimal.ZERO) {
                    getIconWithColor(FontAwesomeIcon.DOWNLOAD, Color.GREEN)
                } else {
                    getIconWithColor(FontAwesomeIcon.UPLOAD, Color.RED)
                }
            }
            else -> null // not a saving (or null) -> no graphic
        }
    }

    private fun displayEditControl(item: Boolean) {

        checkBox.isSelected = item
        graphic = checkBox
    }

}