package de.omilke.bankingfx.main.converter

import de.omilke.banking.account.entity.EntrySequence
import de.omilke.bankingfx.UIConstants
import de.omilke.bankingfx.main.entrylist.model.Entry
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
    private val items: ObservableList<Entry>,
    private val entry: Entry
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

        prepare(entry)

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

    private fun prepare(entry: Entry) {

        entryComment.text = entry.comment
        entryCategory.text = entry.category
        entryDate.text = entry.entryDate.format(UIConstants.DATE_FORMATTER)

        entryAmount.text = entry.amount.toPlainString()
        applyColor(entryAmount, entry.amount < BigDecimal.ZERO)

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
            val signumDiffers = newValue.signum() != entry.amount.signum()
                    //essentially avoid warning when value is 0
                    && newValue.compareTo(BigDecimal.ZERO) != 0

            definedAmount.left.isVisible = signumDiffers

            applyColor(definedAmount, newValue < BigDecimal.ZERO)
        }

    }

    private fun updateLeftoverAmount(newValue: BigDecimal?) {

        if (newValue != null) {
            val delta = entry.amount - newValue
            leftoverAmount.text = delta.toString()
            leftoverAmount.left.isVisible = false

            applyColor(leftoverAmount, delta < BigDecimal.ZERO)
        } else {
            leftoverAmount.left.isVisible = true
            leftoverAmount.text = ""
        }
    }

    private fun applyColor(element: Node, isNegative: Boolean) {

        element.styleClass.remove(UIConstants.POSITIVE)
        element.styleClass.remove(UIConstants.NEGATIVE)

        if (isNegative) {
            element.styleClass.add(UIConstants.NEGATIVE)
        } else {
            element.styleClass.add(UIConstants.POSITIVE)
        }
    }

    @FXML
    fun cancel() {
        this.hide()
    }

    @FXML
    fun performSplit() {

        val originalIndex = items.indexOf(entry)

        items.remove(entry)
        items.add(originalIndex, splitEntryWithAmount(entry, definedAmount.text.toBigDecimal()))
        items.add(originalIndex + 1, splitEntryWithAmount(entry, leftoverAmount.text.toBigDecimal()))

        hide()
    }

    private fun splitEntryWithAmount(entry: Entry, newAmount: BigDecimal): Entry {

        //EntryOrder.of(entryDate, sequence, orderIndex)
        return Entry(
            entry.entryDate,
            entry.entryOrder.sequence,
            entry.entryOrder.orderIndex,
            newAmount,
            entry.saving,
            entry.category,
            entry.comment
        )
    }

}