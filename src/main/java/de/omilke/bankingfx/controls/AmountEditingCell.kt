package de.omilke.bankingfx.controls

import javafx.event.EventHandler
import javafx.scene.control.TableCell
import javafx.scene.control.TextField
import javafx.scene.paint.Color
import java.math.BigDecimal
import java.util.*

class AmountEditingCell<P>(locale: Locale) : TableCell<P, BigDecimal?>() {

    private val converter: BigDecimalStringConverter
    private var text: TextField = TextField()

    init {
        createComboBox()

        converter = BigDecimalStringConverter(locale)
    }

    override fun startEdit() {

        if (!isEmpty) {
            super.startEdit()

            displayEditControl(item)

            this.text.requestFocus()
        }
    }

    override fun cancelEdit() {

        super.cancelEdit()

        displayValue(item!!)
    }

    override fun updateItem(item: BigDecimal?, empty: Boolean) {

        super.updateItem(item, empty)

        if (empty || item == null) {
            graphic = null
            setText(null)
        } else {
            if (isEditing) {
                displayEditControl(item)
            } else {
                displayValue(item)
            }
        }
    }

    private fun displayValue(item: BigDecimal) {

        setText(converter.toString(item))

        if (item >= BigDecimal.ZERO) {
            setTextFill(Color.GREEN)
        } else {
            setTextFill(Color.RED)
        }

        graphic = null
    }

    private fun displayEditControl(item: BigDecimal?) {

        text.text = converter.toString(item)

        setText(null)
        graphic = text
    }

    private fun createComboBox() {

        text.onAction = EventHandler {
            val value = text.textProperty().value

            val fromString = converter.fromString(value)

            if (fromString != null) {
                commitEdit(fromString)
            } else {
                cancelEdit()
            }
        }
    }
}