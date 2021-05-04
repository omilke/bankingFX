package de.omilke.bankingfx.main.sequenceeditor

import de.omilke.banking.account.entity.EntrySequence
import de.omilke.bankingfx.main.sequenceeditor.model.EntryOrderSetting
import de.saxsys.mvvmfx.FluentViewLoader
import de.saxsys.mvvmfx.FxmlView
import javafx.fxml.FXML
import javafx.scene.control.*
import javafx.util.Callback
import java.util.*

class SequenceeditorView : FxmlView<SequenceeditorModel?> {

    @FXML
    lateinit var dialog: DialogPane

    @FXML
    lateinit var sequence: ChoiceBox<EntrySequence>

    @FXML
    lateinit var order: TextField

    fun initialize() {

        initSequence(sequence)
        initOrder(order)
    }

    private fun initSequence(choiceBox: ChoiceBox<EntrySequence>) {

        choiceBox.items.clear()
        choiceBox.items.addAll(*EntrySequence.values())

        choiceBox.value = EntrySequence.REGULAR
    }

    private fun initOrder(textfield: TextField) {

        textfield.text = "0"
    }

    private fun provide(buttonType: ButtonType): EntryOrderSetting? {

        return when (buttonType) {
            ButtonType.APPLY -> try {
                this.result
            } catch (e: InvalidEntryOrderException) {
                null
            }
            else -> null
        }
    }

    @get:Throws(InvalidEntryOrderException::class)
    private val result: EntryOrderSetting
        get() {

            val text = order.text

            return try {
                val orderIndex = text.toInt()

                // exactly two digit
                if (orderIndex in 0..99) {
                    EntryOrderSetting(sequence.value, orderIndex)
                } else {
                    throw InvalidEntryOrderException(orderIndex)
                }
            } catch (e: NumberFormatException) {
                throw InvalidEntryOrderException(text)
            }
        }

    private fun initValues(sequence: EntrySequence?, orderIndex: Int?) {

        this.sequence.value = sequence
        order.text = orderIndex?.toString()
    }

    companion object {

        fun openView(sequence: EntrySequence?, orderIndex: Int?): Optional<EntryOrderSetting> {

            val viewTuple = FluentViewLoader.fxmlView(SequenceeditorView::class.java).load()
            val view = viewTuple.codeBehind

            view.initValues(sequence, orderIndex)

            val dialog = Dialog<EntryOrderSetting>()
            dialog.dialogPane = viewTuple.view as DialogPane
            dialog.resultConverter = Callback { view.provide(it) }

            return dialog.showAndWait()
        }
    }
}