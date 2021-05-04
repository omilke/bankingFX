package de.omilke.bankingfx.controls

import javafx.event.EventHandler
import javafx.scene.control.DatePicker
import javafx.scene.control.TableCell
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class DateEditingCell<P>(private val formatter: DateTimeFormatter) : TableCell<P, LocalDate?>() {

    //TODO: why lazily constructed?
    private var datePicker: DatePicker? = null

    override fun startEdit() {

        if (!isEmpty) {
            super.startEdit()
            createDatePicker()

            displayEditControl(item)

            datePicker!!.show()
        }
    }

    override fun cancelEdit() {

        super.cancelEdit()

        displayValue(item!!)
    }

    override fun updateItem(item: LocalDate?, empty: Boolean) {

        super.updateItem(item, empty)

        if (empty || item == null) {
            text = null
            setGraphic(null)
        } else {
            if (isEditing) {
                displayEditControl(item)
            } else {
                displayValue(item)
            }
        }
    }

    private fun displayValue(item: LocalDate) {

        text = item.format(formatter)
        graphic = null
    }

    private fun displayEditControl(item: LocalDate?) {

        datePicker?.let {
            it.value = item
        }

        text = null
        graphic = datePicker
    }

    private fun createDatePicker() {

        datePicker = DatePicker(item)
        datePicker!!.onAction = EventHandler { commitEdit(datePicker!!.value) }
    }
}