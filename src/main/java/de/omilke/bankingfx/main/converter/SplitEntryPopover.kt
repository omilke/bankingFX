package de.omilke.bankingfx.main.converter

import de.omilke.bankingfx.UIConstants
import de.omilke.bankingfx.controls.UIUtils
import de.omilke.bankingfx.main.entrylist.model.EntryTableRow
import javafx.collections.ObservableList
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.Node
import javafx.scene.control.Button
import javafx.scene.control.TextField
import javafx.scene.layout.Region
import javafx.scene.layout.VBox
import org.controlsfx.control.PopOver
import org.controlsfx.control.textfield.CustomTextField
import java.io.IOException
import java.math.BigDecimal

class SplitEntryPopover(
        private val items: ObservableList<EntryTableRow>,
        private val entryTableRow: EntryTableRow
) : PopOver(VBox()) {

    @FXML
    private lateinit var entryComment: TextField

    @FXML
    private lateinit var entryCategory: TextField

    @FXML
    private lateinit var entryDate: TextField

    @FXML
    private lateinit var entryAmount: TextField

    @FXML
    private lateinit var definedAmount: CustomTextField

    @FXML
    private lateinit var leftoverAmount: CustomTextField

    @FXML
    private lateinit var splitButton: Button

    init {
        loadFxml()

        setupPopover()

        prepare(entryTableRow)

    }

    private fun loadFxml() {

        val fxmlLoader = FXMLLoader(javaClass.getResource("SplitentryPopover.fxml"))

        fxmlLoader.setRoot(this.contentNode)
        fxmlLoader.setController(this)

        try {
            fxmlLoader.load<Nothing>()
        } catch (exception: IOException) {
            throw RuntimeException(exception)
        }
    }

    private fun setupPopover() {

        title = "Splitting entry"

        arrowLocation = ArrowLocation.LEFT_CENTER
        arrowSize = 1.0
        cornerRadius = 10.0
        isAnimated = true
        isHeaderAlwaysVisible = true
        isHideOnEscape = true

        prefWidth(Region.USE_COMPUTED_SIZE)
        prefHeight(Region.USE_COMPUTED_SIZE)

        splitButton.isDefaultButton = true
    }

    private fun prepare(entryTableRow: EntryTableRow) {

        entryComment.text = entryTableRow.getComment()
        entryCategory.text = entryTableRow.getCategory()
        entryDate.text = entryTableRow.getEntryDate().format(UIConstants.DATE_FORMATTER)

        entryAmount.text = entryTableRow.getAmount()?.toPlainString()
        applyColor(entryAmount, entryTableRow.getAmount())

        definedAmount.textProperty().addListener { _, _, newValue -> onAmountChanged(newValue) }
        definedAmount.text = "0"

        definedAmount.requestFocus()
    }

    private fun onAmountChanged(newValue: String?) {

        val parsedAmount = newValue?.toBigDecimalOrNull()

        checkDefinedAmount(parsedAmount)
        updateLeftoverAmount(parsedAmount)

        this.splitButton.isDisable = parsedAmount == null
    }

    private fun checkDefinedAmount(newValue: BigDecimal?) {

        if (newValue != null) {
            val signumDiffers = newValue.signum() != entryTableRow.getAmount()!!.signum()
                    //essentially avoid warning when value is 0
                    && newValue.compareTo(BigDecimal.ZERO) != 0

            definedAmount.left.isVisible = signumDiffers

            applyColor(definedAmount, newValue)
        }
    }

    private fun updateLeftoverAmount(newValue: BigDecimal?) {

        if (newValue != null) {
            val delta = entryTableRow.getAmount()!! - newValue
            leftoverAmount.text = delta.toString()
            leftoverAmount.left.isVisible = false

            applyColor(leftoverAmount, delta)
        } else {
            leftoverAmount.left.isVisible = true
            leftoverAmount.text = ""
        }
    }

    private fun applyColor(element: Node, value: BigDecimal?) {

        element.styleClass.remove(UIConstants.POSITIVE)
        element.styleClass.remove(UIConstants.NEGATIVE)

        if (UIUtils.isPositive(value)) {
            element.styleClass.add(UIConstants.POSITIVE)
        } else {
            element.styleClass.add(UIConstants.NEGATIVE)
        }
    }

    @FXML
    fun cancel() {
        this.hide()
    }

    @FXML
    fun performSplit() {

        val originalIndex = items.indexOf(entryTableRow)

        items.remove(entryTableRow)
        items.add(originalIndex, splitEntryWithAmount(entryTableRow, definedAmount.text.toBigDecimal()))
        items.add(originalIndex + 1, splitEntryWithAmount(entryTableRow, leftoverAmount.text.toBigDecimal()))

        hide()
    }

    private fun splitEntryWithAmount(entryTableRow: EntryTableRow, newAmount: BigDecimal): EntryTableRow {

        return EntryTableRow(
                entryTableRow.getEntryDate(),
                entryTableRow.getEntryOrder(),
                newAmount,
                entryTableRow.getSaving(),
                entryTableRow.getCategory(),
                entryTableRow.getComment(),
                false
        )
    }

}