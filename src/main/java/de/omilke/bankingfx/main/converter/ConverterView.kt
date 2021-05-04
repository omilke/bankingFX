package de.omilke.bankingfx.main.converter

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView
import de.omilke.banking.BankingConfigurator.configuredLocale
import de.omilke.banking.account.EntryService
import de.omilke.banking.account.entity.EntrySequence
import de.omilke.banking.interop.exporting.LinesWriter.saveLinesToFile
import de.omilke.banking.interop.exporting.jhaushalt.JHaushaltFormatter
import de.omilke.banking.interop.importing.parser.ingdiba.IngDibaCsvExportConverter
import de.omilke.bankingfx.UIConstants
import de.omilke.bankingfx.controls.AmountEditingCell
import de.omilke.bankingfx.controls.DateEditingCell
import de.omilke.bankingfx.controls.DefaultFileChooser
import de.omilke.bankingfx.controls.SavingEditingCell
import de.omilke.bankingfx.controls.UIUtils.getIcon
import de.omilke.bankingfx.main.entrylist.model.EntryTableRow
import de.omilke.bankingfx.main.entrylist.model.EntryOrder
import de.omilke.bankingfx.main.sequenceeditor.SequenceeditorView
import de.omilke.bankingfx.main.sequenceeditor.model.EntryOrderSetting
import de.saxsys.mvvmfx.FxmlView
import javafx.beans.binding.Bindings
import javafx.beans.property.SimpleBooleanProperty
import javafx.collections.FXCollections
import javafx.collections.ListChangeListener
import javafx.collections.ObservableList
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.fxml.FXML
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Node
import javafx.scene.control.*
import javafx.scene.control.cell.ComboBoxTableCell
import javafx.scene.control.cell.TextFieldTableCell
import javafx.scene.layout.HBox
import javafx.scene.layout.StackPane
import javafx.stage.FileChooser
import org.apache.logging.log4j.Level
import org.apache.logging.log4j.LogManager
import java.io.File
import java.io.IOException
import java.math.BigDecimal
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.time.LocalDate
import java.util.*
import de.omilke.banking.account.entity.Entry as domainEntry

class ConverterView : FxmlView<ConverterModel> {

    private val es = EntryService

    private val locale = configuredLocale()

    @FXML
    lateinit var postponeCheckBox: CheckBox

    @FXML
    lateinit var entryCounterLabel: Label

    @FXML
    lateinit var entryTable: TableView<EntryTableRow>

    fun initialize() {

        val categories = es.getAllCategories()
        setColumns(categories)

        entryCounterLabel.textProperty().bind(Bindings.size(entryTable.items).asString())
    }

    @FXML
    fun actionSaveToFile() {

        val fileChooser = DefaultFileChooser(
                "converted.csv",
                entryTable.scene.window,
                "Select Save File",
                FileChooser.ExtensionFilter("CSV files", "*.csv"))

        saveToSelectedFile(fileChooser.showSave())
    }

    private fun saveToSelectedFile(result: File?) {

        result?.let {
            saveLinesToFile(it, formatEntries(entryTable.items))
        }
    }

    private fun formatEntries(entriesToExport: List<EntryTableRow>): List<String> {

        //TODO: supply dictionary for comment -> category to ease import in JHaushalt
        val formatter = JHaushaltFormatter(false)

        val result: MutableList<String> = ArrayList()
        for (currentEntry in entriesToExport) {

            val entryDate: LocalDate = when {
                postponeCheckBox.isSelected -> currentEntry.getEntryDate().plusYears(1)
                else -> currentEntry.getEntryDate()
            }

            result.add(formatter.format(entryDate, currentEntry.getAmount()!!, currentEntry.getComment(), currentEntry.getCategory()))
        }

        return result
    }

    @FXML
    fun actionOpenFileForConversion() {

        val fileChooser = DefaultFileChooser(
                "",
                entryTable.scene.window,
                "Select File For Conversion",
                FileChooser.ExtensionFilter("all file types (*.*)", "*.*"))

        convertSelectedFile(fileChooser.showOpen())
    }

    private fun convertSelectedFile(selectedFile: File?) {

        selectedFile?.let {

            val path = it.toPath()

            try {
                val lines = Files.readAllLines(path, StandardCharsets.ISO_8859_1)

                performConversion(lines)
            } catch (e: IOException) {
                val logger = LogManager.getLogger(this.javaClass)
                logger.log(Level.WARN, "File could not be read with ANSI encoding: {}", path.toString(), e)
            }
        }
    }

    private fun performConversion(lines: List<String>) {

        val convertedEntries = IngDibaCsvExportConverter().convert(lines)
        fillTable(convertedEntries)
    }

    private fun fillTable(parsedEntries: List<domainEntry>) {

        val entries: List<EntryTableRow> = parsedEntries
                .map(::EntryTableRow)

        entryTable.items.clear()
        entryTable.items.addAll(entries)
    }

    private fun setColumns(categories: List<String>) {

        val dateColumn = TableColumn<EntryTableRow, LocalDate>("Date")
        dateColumn.prefWidth = UIConstants.MONTH_WIDTH
        dateColumn.styleClass.add(UIConstants.ALIGN_CENTER)
        dateColumn.setCellValueFactory { param -> param.value.entryDateProperty() }
        dateColumn.setCellFactory { DateEditingCell(UIConstants.DATE_FORMATTER) }
        dateColumn.setOnEditCommit { e: TableColumn.CellEditEvent<EntryTableRow?, LocalDate> -> e.tableView.items[e.tablePosition.row]!!.setEntryDate(e.newValue) }

        val sequenceColumn = TableColumn<EntryTableRow, EntryOrder>("")
        sequenceColumn.prefWidth = UIConstants.SEQUENCE_WIDTH
        sequenceColumn.styleClass.add(UIConstants.ALIGN_CENTER)
        sequenceColumn.setCellValueFactory { param -> param.value.entryOrderProperty() }
        sequenceColumn.setCellFactory {
            object : TableCell<EntryTableRow, EntryOrder?>() {
                override fun updateItem(item: EntryOrder?, empty: Boolean) {

                    super.updateItem(item, empty)

                    val rowItem = tableRow.item
                    if (empty || item == null || rowItem == null) {
                        graphic = null
                    } else {
                        graphic = when (item.sequence) {
                            EntrySequence.FIRST -> getIcon(FontAwesomeIcon.CHEVRON_CIRCLE_UP)
                            EntrySequence.LAST -> getIcon(FontAwesomeIcon.CHEVRON_CIRCLE_DOWN)
                            else -> null
                        }

                        val entryOrder = rowItem.getEntryOrder()

                        val addMenu = createContextMenu {
                            val entryOrderSetting: Optional<EntryOrderSetting> = SequenceeditorView.openView(entryOrder?.sequence, entryOrder?.orderIndex!!)
                            entryOrderSetting.ifPresent { p: EntryOrderSetting -> rowItem.setEntryOrder(entryOrder.update(p.entrySequence, p.orderIndex)) }
                        }

                        contextMenu = addMenu
                    }
                }
            }
        }

        val amountColumn = TableColumn<EntryTableRow, BigDecimal>("Amount")
        amountColumn.prefWidth = UIConstants.AMOUNT_WIDTH
        amountColumn.styleClass.add(UIConstants.ALIGN_RIGHT)
        amountColumn.setCellValueFactory { param -> param.value.amountProperty() }
        amountColumn.setCellFactory { AmountEditingCell(locale) }
        amountColumn.setOnEditCommit { e: TableColumn.CellEditEvent<EntryTableRow, BigDecimal> -> e.tableView.items[e.tablePosition.row]!!.setAmount(e.newValue) }

        val savingColumn = TableColumn<EntryTableRow, Boolean>("Saving")
        savingColumn.prefWidth = UIConstants.SAVING_WIDTH
        savingColumn.styleClass.add(UIConstants.ALIGN_CENTER)
        savingColumn.setCellValueFactory { param -> param.value.savingProperty() }
        savingColumn.setCellFactory { SavingEditingCell() }
        savingColumn.setOnEditCommit { e: TableColumn.CellEditEvent<EntryTableRow?, Boolean> -> e.tableView.items[e.tablePosition.row]!!.setSaving(e.newValue) }

        val categoryColumn = TableColumn<EntryTableRow, String>("Category")
        categoryColumn.prefWidth = UIConstants.CATEGORY_WIDTH
        categoryColumn.styleClass.add(UIConstants.ALIGN_LEFT)
        categoryColumn.setCellValueFactory { param -> param.value.categoryProperty() }
        categoryColumn.cellFactory = ComboBoxTableCell.forTableColumn(FXCollections.observableList(categories))
        categoryColumn.setOnEditCommit { e: TableColumn.CellEditEvent<EntryTableRow, String> -> e.tableView.items[e.tablePosition.row]!!.setCategory(e.newValue) }

        val commentColumn = TableColumn<EntryTableRow, String>("Comment")
        commentColumn.prefWidth = UIConstants.COMMENT_IMPORT_WIDTH
        commentColumn.styleClass.add(UIConstants.ALIGN_LEFT)
        commentColumn.setCellValueFactory { param -> param.value!!.commentProperty() }
        commentColumn.setCellFactory(TextFieldTableCell.forTableColumn())
        commentColumn.setOnEditCommit { e: TableColumn.CellEditEvent<EntryTableRow?, String> -> e.tableView.items[e.tablePosition.row]!!.setComment(e.newValue) }

        val buttonBarCell = TableColumn<EntryTableRow, Boolean>("")
        buttonBarCell.prefWidth = UIConstants.ACTION_IMPORT_WIDTH
        buttonBarCell.styleClass.add(UIConstants.ALIGN_CENTER)
        buttonBarCell.isSortable = false

        // define a simple boolean cell value for the action column so that the column will only be shown for non-empty rows.
        buttonBarCell.setCellValueFactory { SimpleBooleanProperty(it.value != null) }

        // getEntryRepository a cell value factory with an add button for each row in the table.
        buttonBarCell.setCellFactory { ButtonBarCell() }

        entryTable.columns.clear()
        entryTable.columns.add(dateColumn)
        entryTable.columns.add(sequenceColumn)
        entryTable.columns.add(amountColumn)
        entryTable.columns.add(savingColumn)
        entryTable.columns.add(categoryColumn)
        entryTable.columns.add(commentColumn)
        entryTable.columns.add(buttonBarCell)
    }

    private fun createContextMenu(eventHandler: EventHandler<ActionEvent>): ContextMenu {

        val menu = ContextMenu()

        val menuItem = MenuItem("Set Entry Order")
        menuItem.onAction = eventHandler

        menu.items.add(menuItem)

        return menu
    }

    /**
     * Displays a Delete button, that removes the line in which the button was activated.
     */
    private inner class ButtonBarCell : TableCell<EntryTableRow, Boolean?>() {

        private val buttonBar = HBox()

        init {
            buttonBar.alignment = Pos.BASELINE_CENTER

            buttonBar.children.add(buildButton(FontAwesomeIcon.TRASH) { deleteButtonPressed() })
            buttonBar.children.add(buildButton(FontAwesomeIcon.COPY) { splitButtonPressed(it) })
        }

        private fun buildButton(icon: FontAwesomeIcon, onAction: EventHandler<ActionEvent>): Node {

            val button = Button()
            button.graphic = FontAwesomeIconView(icon, "1.25em")
            button.contentDisplay = ContentDisplay.CENTER
            button.onAction = onAction

            val paddedButton = StackPane(button)
            paddedButton.padding = Insets(3.0)

            return paddedButton
        }

        override fun updateItem(item: Boolean?, empty: Boolean) {

            super.updateItem(item, empty)

            if (empty || item == null) {
                graphic = null
            } else {
                //places the button in the row only if the row is not empty.
                contentDisplay = ContentDisplay.GRAPHIC_ONLY
                graphic = buttonBar
            }
        }

        private fun deleteButtonPressed() {

            tableView.items.removeAt(index)
        }

        private fun splitButtonPressed(actionEvent: ActionEvent) {

            SplitEntryPopover(tableView.items as ObservableList<EntryTableRow>, tableRow.item!!).show(actionEvent.source as Node)
        }

    }
}