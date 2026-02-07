package de.omilke.bankingfx.main.entrylist

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon
import de.omilke.banking.account.entity.EntrySequence
import de.omilke.banking.persistence.PersistenceServiceProvider.persistenceService
import de.omilke.bankingfx.UIConstants
import de.omilke.bankingfx.controls.AmountTreeTableCell
import de.omilke.bankingfx.controls.ResizeableColumnCallback
import de.omilke.bankingfx.controls.UIUtils.getIcon
import de.omilke.bankingfx.controls.UIUtils.getIconWithColor
import de.omilke.bankingfx.controls.extensions.atStartOfMonth
import de.omilke.bankingfx.main.entrylist.model.EntryOrder
import de.omilke.bankingfx.main.entrylist.model.EntryTableRow
import de.saxsys.mvvmfx.FxmlView
import javafx.event.EventHandler
import javafx.fxml.FXML
import javafx.scene.control.*
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent
import javafx.scene.input.MouseEvent
import javafx.scene.input.MouseEvent.MOUSE_CLICKED
import javafx.scene.paint.Color
import java.math.BigDecimal
import java.time.LocalDate


class EntrylistView : FxmlView<EntrylistModel> {

    private val er = persistenceService.entryRepository

    @FXML
    lateinit var filter: TextField

    @FXML
    lateinit var collapseIncrease: Button

    @FXML
    lateinit var collapseDecrease: Button


    @FXML
    lateinit var entryTable: TreeTableView<EntryTableRow>

    @FXML
    lateinit var entryCountLabel: Label

    private var entries: List<EntryTableRow> = ArrayList()

    private var expandedItem = 0

    fun initialize() {

        entryTable.root = TreeItem(EntryTableRow())
        entryTable.isShowRoot = false

        entryTable.columnResizePolicy = ResizeableColumnCallback()

        setColumns()
        refreshEntries()

        entryTable.sceneProperty().addListener { _, _, scene ->
            if (scene != null) {
                scene.onKeyPressed = updateOnShift()
                scene.onKeyReleased = updateOnShift()
            }
        }

        collapseIncrease.text = "+▼"
        collapseIncrease.addEventHandler(MOUSE_CLICKED, ::increaseCollapse)

        collapseDecrease.text = "-▼"
        collapseDecrease.addEventHandler(MOUSE_CLICKED, ::decreaseCollapse)
    }

    private fun refreshEntries() {

        entries = er
            .findAllEntries()
            .map(::EntryTableRow)

        fillTable(entries)
    }

    private fun fillTable(entries: List<EntryTableRow>) {

        entryTable.root.children.clear()

        var previousGroup: LocalDate? = null
        var currentRoot = TreeItem<EntryTableRow>()

        for (entry in entries) {
            val currentGroup = entry.getEntryDate().atStartOfMonth()
            if (currentGroup != previousGroup) {
                //memorize to tree-item-root to add items to
                currentRoot = TreeItem(EntryTableRow(currentGroup))
                entryTable.root.children.add(currentRoot)

                previousGroup = currentGroup
            }

            currentRoot.children.add(TreeItem(entry))
        }

        expandedItem = 0
        uncollapseRows()

        entryCountLabel.text = "${entries.size}"
    }

    private fun uncollapseRows() {

        var expanded = 0
        for (item in entryTable.root.children) {

            if (isInFutureMonth(item.value.getEntryDate()))
                item.isExpanded = false
            else
                if (expanded >= MONTH_TO_EXPAND + expandedItem)
                    item.isExpanded = false
                else {
                    item.isExpanded = true
                    expanded++
                }
        }
    }

    fun isInFutureMonth(key: LocalDate): Boolean {

        val firstOfCurrentMonth = LocalDate.now().atStartOfMonth()
        return key.atStartOfMonth().isAfter(firstOfCurrentMonth)
    }

    private fun setColumns() {

        entryTable.setRowFactory {
            object : TreeTableRow<EntryTableRow?>() {
                override fun updateItem(item: EntryTableRow?, empty: Boolean) {

                    super.updateItem(item, empty)

                    styleClass.removeAll(UIConstants.GROUP_ROW)
                    if (!empty && item != null && item.isGroupElement) {
                        styleClass.add(UIConstants.GROUP_ROW)
                    }
                }
            }
        }

        val dateColumn = TreeTableColumn<EntryTableRow, String>("Date")
        dateColumn.prefWidth = UIConstants.MONTH_WIDTH
        dateColumn.styleClass.add(UIConstants.DESCRIPTION_COLUMN)
        dateColumn.setCellValueFactory { it.value.value.getDescriptionProperty() }

        val sequenceColumn = TreeTableColumn<EntryTableRow, EntryOrder?>("")
        sequenceColumn.prefWidth = UIConstants.SEQUENCE_WIDTH
        sequenceColumn.styleClass.add(UIConstants.ALIGN_CENTER)
        sequenceColumn.setCellValueFactory { it.value.value.entryOrderProperty() }
        sequenceColumn.setCellFactory {
            object : TreeTableCell<EntryTableRow?, EntryOrder?>() {
                override fun updateItem(item: EntryOrder?, empty: Boolean) {

                    super.updateItem(item, empty)

                    graphic = if (empty || item == null) {
                        null
                    } else {

                        //TODO maybe this could be solved via RowFactory + CSS
                        // but that depends on the possibilities to really style the content with glyph font
                        // however, there should be an example with the search-bar styling
                        when (item.sequence) {
                            EntrySequence.FIRST -> getIcon(FontAwesomeIcon.CHEVRON_CIRCLE_UP)
                            EntrySequence.LAST -> getIcon(FontAwesomeIcon.CHEVRON_CIRCLE_DOWN)
                            else -> null
                        }
                    }
                }
            }
        }

        val amountColumn = TreeTableColumn<EntryTableRow, BigDecimal?>("Amount")
        amountColumn.prefWidth = UIConstants.AMOUNT_WIDTH
        amountColumn.setCellValueFactory { it.value.value.amountProperty() }
        amountColumn.setCellFactory { AmountTreeTableCell() }

        val savingColumn = TreeTableColumn<EntryTableRow, Boolean>("Saving")
        savingColumn.prefWidth = UIConstants.SAVING_WIDTH
        savingColumn.styleClass.add(UIConstants.ALIGN_CENTER)
        savingColumn.setCellValueFactory { it.value.value.savingProperty() }
        savingColumn.setCellFactory {
            object : TreeTableCell<EntryTableRow, Boolean?>() {
                override fun updateItem(item: Boolean?, empty: Boolean) {

                    super.updateItem(item, empty)

                    if (empty || item == null || !item) {
                        graphic = null
                    } else {
                        val amount = tableRow.item.getAmount()!!

                        //TODO maybe this could be solved via RowFactory + CSS
                        // but that depends on the possibilities to really style the content with glyph font
                        graphic = when {
                            amount < BigDecimal.ZERO -> getIconWithColor(FontAwesomeIcon.DOWNLOAD, Color.GREEN)
                            else -> getIconWithColor(FontAwesomeIcon.UPLOAD, Color.RED)
                        }
                    }
                }
            }
        }

        val categoryColumn = TreeTableColumn<EntryTableRow, String>("Category")
        categoryColumn.prefWidth = UIConstants.CATEGORY_WIDTH
        categoryColumn.setCellValueFactory { it.value.value.categoryProperty() }

        val commentColumn = TreeTableColumn<EntryTableRow, String>("Comment")
        commentColumn.prefWidth = UIConstants.COMMENT_WIDTH
        commentColumn.setCellValueFactory { it.value.value.commentProperty() }

        entryTable.columns.clear()
        entryTable.columns.add(dateColumn)
        entryTable.columns.add(sequenceColumn)
        entryTable.columns.add(amountColumn)
        entryTable.columns.add(savingColumn)
        entryTable.columns.add(categoryColumn)
        entryTable.columns.add(commentColumn)
    }

    @FXML
    fun filter() {

        fillTable(
            entries
                .filter(this::matchFilter)
        )
    }

    @FXML
    fun handleEscape(keyEvent: KeyEvent) {

        if (keyEvent.code == KeyCode.ESCAPE) {
            clearFilter()
        }
    }

    @FXML
    fun clearFilter() {

        filter.text = ""
        this.filter()
    }

    private fun matchFilter(entryTableRow: EntryTableRow): Boolean {

        val filterExpression = filter.text

        return when {
            filterExpression == null || filterExpression.isEmpty() -> true
            else -> entryTableRow.getComment().contains(filterExpression, true) || entryTableRow.getCategory()
                .contains(filterExpression, true)
        }
    }

    fun increaseCollapse(event: MouseEvent) {

        this.expandedItem +=
            if (event.isAltDown) 12
            else 1

        uncollapseRows()
    }

    fun decreaseCollapse(event: MouseEvent) {

        this.expandedItem -=
            if (event.isAltDown) 12
            else 1

        expandedItem = expandedItem.coerceAtLeast(0)

        uncollapseRows()
    }

    private fun updateOnShift(): EventHandler<KeyEvent?> = EventHandler { keyEvent: KeyEvent ->

        if (keyEvent.isAltDown) {
            collapseIncrease.text = "+▼▼"
            collapseDecrease.text = "-▼▼"
        } else {
            collapseIncrease.text = "+▼ "
            collapseDecrease.text = "-▼ "
        }
    }

    companion object {

        private const val MONTH_TO_EXPAND = 3
    }
}