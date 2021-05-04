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
import de.omilke.bankingfx.main.entrylist.model.buildEntryOrder
import de.saxsys.mvvmfx.FxmlView
import javafx.fxml.FXML
import javafx.geometry.Pos
import javafx.scene.control.*
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent
import javafx.scene.paint.Color
import java.math.BigDecimal
import java.time.LocalDate
import java.util.*

class EntrylistView : FxmlView<EntrylistModel> {

    private val er = persistenceService.entryRepository

    @FXML
    lateinit var filter: TextField

    @FXML
    lateinit var entryTable: TreeTableView<EntryTableRow>

    @FXML
    lateinit var entryCountLabel: Label

    private var entries: List<EntryTableRow> = ArrayList()

    fun initialize() {

        entryTable.root = TreeItem(EntryTableRow())
        entryTable.isShowRoot = false

        entryTable.columnResizePolicy = ResizeableColumnCallback()

        setColumns()
        refreshEntries()
    }

    private fun refreshEntries() {

        entries = er
                .findAllEntries()
                .map(::EntryTableRow)

        fillTable(entries)
    }

    private fun fillTable(entries: List<EntryTableRow>) {

        entryTable.root.children.clear()

        var previous: LocalDate? = null
        var currentRoot = TreeItem<EntryTableRow>()

        var expandedMonths = 0
        for (entry in entries) {
            val key = entry.getEntryDate().atStartOfMonth()
            if (key != previous) {
                currentRoot = TreeItem(EntryTableRow(key))

                val expanded: Boolean = when {
                    isInFutureMonth(key) -> false // keep future month collapsed...
                    else -> expandedMonths++ <= MONTH_TO_EXPAND // ... but only expand so many month
                }

                currentRoot.isExpanded = expanded
                entryTable.root.children.add(currentRoot)
                previous = key
            }

            currentRoot.children.add(TreeItem(entry))
        }
        entryCountLabel.text = entries.size.toString() + ""
    }

    fun isInFutureMonth(key: LocalDate): Boolean {

        val firstOfCurrentMonth = LocalDate.now().atStartOfMonth()
        return key.atStartOfMonth().isAfter(firstOfCurrentMonth)
    }

    private fun setColumns() {

        val dateColumn = TreeTableColumn<EntryTableRow, LocalDate>("Date")
        dateColumn.prefWidth = UIConstants.MONTH_WIDTH
        dateColumn.setCellValueFactory { it.value.value.entryDateProperty() }
        dateColumn.setCellFactory {
            object : TreeTableCell<EntryTableRow, LocalDate?>() {
                override fun updateItem(item: LocalDate?, empty: Boolean) {

                    super.updateItem(item, empty)

                    if (empty || item == null) {
                        text = null
                    } else {
                        val rowItem = treeTableRow.item
                        val isGroupElement = rowItem.isGroupElement
                        //TODO aligment could be solved via RowFactory + CSS
                        //TODO content display could actually be decided by Model
                        if (isGroupElement) {
                            text = item.format(UIConstants.MONTH_NAME_FORMATTER)
                            alignment = Pos.CENTER_LEFT
                        } else {
                            text = item.format(UIConstants.DATE_FORMATTER)
                            alignment = Pos.CENTER
                        }
                    }
                }
            }
        }

        val sequenceColumn = TreeTableColumn<EntryTableRow, EntryOrder?>("")
        sequenceColumn.prefWidth = UIConstants.SEQUENCE_WIDTH
        sequenceColumn.styleClass.add(UIConstants.ALIGN_CENTER)
        sequenceColumn.setCellValueFactory { it.value.value.entryOrderProperty() }
        sequenceColumn.setCellFactory {
            object : TreeTableCell<EntryTableRow?, EntryOrder?>() {
                override fun updateItem(item: EntryOrder?, empty: Boolean) {

                    super.updateItem(item, empty)

                    if (empty || item == null) {
                        graphic = null
                    } else {

                        //TODO maybe this could be solved via RowFactory + CSS
                        // but that depends on the possibilities to really style the content with glyph font
                        //however, there should be an example with the search-bar styling
                        graphic = when (item.sequence) {
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
                        val amount = treeTableRow.item.getAmount()!!

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

        fillTable(entries
                .filter(this::matchFilter)
                .toList()
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
            else -> entryTableRow.getComment().equals(filterExpression, true) || entryTableRow.getCategory().equals(filterExpression, true)
        }
    }

    companion object {

        private const val MONTH_TO_EXPAND = 3
    }
}