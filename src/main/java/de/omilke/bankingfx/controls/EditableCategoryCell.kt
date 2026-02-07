package de.omilke.bankingfx.controls

import javafx.beans.Observable
import javafx.collections.ObservableList
import javafx.event.EventHandler
import javafx.event.EventType
import javafx.scene.control.TableCell
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent
import javafx.scene.input.MouseEvent
import org.controlsfx.control.SearchableComboBox

class EditableCategoryCell<P>(private val items: ObservableList<String?>) : TableCell<P, String?>() {

    private var comboBox: SearchableComboBox<String?>? = null

    override fun startEdit() {

        super.startEdit()

        if (isEditing) {
            displayEditControl(item)

            println("edit started")
        }
    }

    override fun cancelEdit() {

        super.cancelEdit()

        text = item
        graphic = null

        println("edit canceled")
    }

    override fun updateItem(item: String?, empty: Boolean) {

        super.updateItem(item, empty)

        if (empty) {
            graphic = null
            text = null
        } else {

            if (isEditing) {
                displayEditControl(item)
            } else {
                displayValue(item)
            }
        }
    }

    override fun commitEdit(newValue: String?) {
        super.commitEdit(newValue)

        println("commited: $newValue")
    }

    private fun displayEditControl(item: String?) {

        println("display for edit: ${tableRow.item}")

        if (comboBox == null) {
            comboBox = createComboBox(this, items)
        }

        if (items.contains(item)) {
            comboBox!!.selectionModel.select(item)
        } else {
            println("select invalid item: $item")
        }

        text = null
        graphic = comboBox

        comboBox!!.requestFocus()
        comboBox!!.show()
    }

    private fun displayValue(item: String?) {

        println("display value: ${tableRow.item}")

        text = item
        graphic = null
    }

    private fun createComboBox(
        cell: EditableCategoryCell<P>,
        items: ObservableList<String?>
    ): SearchableComboBox<String?> {

        val result = SearchableComboBox<String?>(items)
        /*
                result.minWidth = width - this.graphicTextGap * 2
        */

        // setup listeners to properly commit any changes back into the data model.
        // First listener attempts to commit or cancel when the ENTER or ESC keys are released.
        // This is applicable in cases where the ComboBox is editable, and the user has
        // typed some input, and also when the ComboBox popup is showing.
        result.addEventFilter<KeyEvent>(
            KeyEvent.KEY_RELEASED
        ) { e: KeyEvent ->
            if (e.code == KeyCode.ENTER) {
                cell.commitEdit(result.value)
            } else if (e.code == KeyCode.ESCAPE) {
                cell.cancelEdit()
            }
        }

        // Second listener attempts to commit when the user is in the editor of
        // the ComboBox, and moves focus away.
        result.editor.focusedProperty().addListener { o: Observable? ->
            if (!result.isFocused) {
                cell.commitEdit(result.value)
            }
        }


        // Third listener makes an assumption about the skin being used, and attempts to add
        // a listener to the ListView within it, such that when the user mouse clicks on a
        // on an item, that is immediately committed and the cell exits the editing mode.
        result.addEventHandler(EventType.ROOT) {
            println("${it.eventType} / ${it.source} / ${it.target} / $it")
        }


        /*
                val success = listenToComboBoxSkin(result, cell)
                if (!success) {
                    result.skinProperty().addListener(object : InvalidationListener {
                        override fun invalidated(observable: Observable) {
                            val successInListener = listenToComboBoxSkin(result, cell)
                            if (successInListener) {
                                result.skinProperty().removeListener(this)
                            }
                        }
                    })
                }
        */


        return result
    }

    /*
        private fun listenToComboBoxSkin(comboBox: SearchableComboBox<String?>, cell: EditableCategoryCell<P>): Boolean {
            val skin = comboBox.skin

            println(skin)

            if (skin != null && skin is ComboBoxListViewSkin<*>) {
                val popupContent = skin.popupContent
                if (popupContent != null && popupContent is ListView<*>) {
                    popupContent.addEventHandler(
                        MouseEvent.MOUSE_RELEASED
                    ) {
                        cell.commitEdit(
                            comboBox.value
                        )
                    }
                    return true
                }
            }
            return false
        }
    */
}
