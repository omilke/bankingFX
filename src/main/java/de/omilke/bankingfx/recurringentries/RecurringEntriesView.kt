package de.omilke.bankingfx.recurringentries

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon
import de.omilke.banking.account.entity.Entry
import de.omilke.banking.account.entity.EntrySequence
import de.omilke.banking.account.entity.RecurringEntry
import de.omilke.banking.interop.exporting.LinesWriter
import de.omilke.banking.interop.exporting.jhaushalt.JHaushaltFormatter
import de.omilke.banking.persistence.PersistenceServiceProvider
import de.omilke.bankingfx.UIConstants
import de.omilke.bankingfx.controls.DefaultFileChooser
import de.omilke.bankingfx.controls.UIUtils
import de.omilke.bankingfx.main.entrylist.model.EntryOrder
import de.omilke.bankingfx.recurringentries.control.SavingTableCell
import de.omilke.bankingfx.recurringentries.model.entryOrder
import de.saxsys.mvvmfx.Context
import de.saxsys.mvvmfx.FxmlView
import de.saxsys.mvvmfx.InjectContext
import de.saxsys.mvvmfx.InjectViewModel
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.collections.ListChangeListener
import javafx.fxml.FXML
import javafx.scene.control.*
import javafx.stage.FileChooser
import javafx.util.Callback
import java.math.BigDecimal
import java.time.LocalDate
import java.util.stream.Collectors

class RecurringEntriesView : FxmlView<RecurringEntriesModel> {

    private val rer = PersistenceServiceProvider.persistenceService.recurringEntryRepository

    @FXML
    private lateinit var fromDate: DatePicker

    @FXML
    private lateinit var toDate: DatePicker

    @FXML
    lateinit var recurringEntryTable: TableView<RecurringEntry>

    @FXML
    lateinit var entryTable: TableView<Entry>

    @FXML
    lateinit var entryCountLabel: Label

    @InjectViewModel
    private lateinit var viewModel: RecurringEntriesModel

    @InjectContext
    private lateinit var context: Context

    fun initialize() {

        initRecurringEntryTable()

        prepareEntryTable()

        val recurringEntries = rer.findAllRecurringEntries()
        recurringEntryTable.items.addAll(recurringEntries)

        setupDateFilter()

        //TODO allow saving of recurring entries --> last recurrence
        //TODO allow saving of entries
    }

    private fun initRecurringEntryTable() {

        val sequenceColumn = TableColumn<RecurringEntry, EntrySequence>("")
        sequenceColumn.prefWidth = UIConstants.SEQUENCE_WIDTH
        sequenceColumn.styleClass.add(UIConstants.ALIGN_CENTER)
        sequenceColumn.setCellValueFactory { SimpleObjectProperty(it.value.sequence) }
        sequenceColumn.setCellFactory {
            object : TableCell<RecurringEntry, EntrySequence>() {

                override fun updateItem(item: EntrySequence?, empty: Boolean) {

                    super.updateItem(item, empty)

                    graphic = when {
                        item == null -> null
                        empty -> null
                        else -> when (item) {
                            EntrySequence.FIRST -> UIUtils.getIcon(FontAwesomeIcon.CHEVRON_CIRCLE_UP)
                            EntrySequence.LAST -> UIUtils.getIcon(FontAwesomeIcon.CHEVRON_CIRCLE_DOWN)
                            else -> null
                        }
                    }
                }
            }
        }

        recurringEntryTable.columns.add(sequenceColumn)

        val orderIndexColumn = TableColumn<RecurringEntry, Int>("Order")
        orderIndexColumn.prefWidth = UIConstants.SEQUENCE_WIDTH
        orderIndexColumn.styleClass.add(UIConstants.ALIGN_CENTER)
        orderIndexColumn.setCellValueFactory { SimpleObjectProperty(it.value.orderIndex) }

        recurringEntryTable.columns.add(orderIndexColumn)


        val amountColumn = TableColumn<RecurringEntry, BigDecimal>("Amount")
        amountColumn.prefWidth = UIConstants.AMOUNT_WIDTH
        amountColumn.setCellValueFactory { SimpleObjectProperty(it.value.amount) }
        amountColumn.setCellFactory {
            object : TableCell<RecurringEntry, BigDecimal>() {

                override fun updateItem(item: BigDecimal?, empty: Boolean) {

                    super.updateItem(item, empty)

                    UIUtils.formatAmount(this, item, empty)
                }
            }
        }

        recurringEntryTable.columns.add(amountColumn)

        val savingColumn = TableColumn<RecurringEntry, Boolean>("Saving")
        savingColumn.prefWidth = UIConstants.SAVING_WIDTH
        savingColumn.setCellValueFactory { SimpleBooleanProperty(it.value.isSaving) }
        savingColumn.setCellFactory { SavingTableCell() }
        recurringEntryTable.columns.add(savingColumn)

        val categoryColumn = TableColumn<RecurringEntry, String>("Category")
        categoryColumn.prefWidth = UIConstants.CATEGORY_COMPACT_WIDTH
        categoryColumn.setCellValueFactory { SimpleObjectProperty(it.value.category) }
        recurringEntryTable.columns.add(categoryColumn)

        val commentColumn = TableColumn<RecurringEntry, String>("Comment")
        commentColumn.prefWidth = UIConstants.COMMENT_COMPACT_WIDTH
        commentColumn.setCellValueFactory { SimpleObjectProperty(it.value.comment) }
        recurringEntryTable.columns.add(commentColumn)

        val recurrenceColumn = TableColumn<RecurringEntry, String>("Recurrence")
        recurrenceColumn.prefWidth = UIConstants.RECURRENCE_WIDTH
        recurrenceColumn.styleClass.add(UIConstants.ALIGN_CENTER)
        recurrenceColumn.setCellValueFactory { SimpleObjectProperty(UIUtils.formatRecurrenceStrategy(it.value.recurrenceStrategy)) }
        recurringEntryTable.columns.add(recurrenceColumn)

        val lastRecurrenceColumn = TableColumn<RecurringEntry, LocalDate>("Last Recurrence")
        lastRecurrenceColumn.prefWidth = UIConstants.RECURRENCE_DATE_WIDTH
        lastRecurrenceColumn.setCellValueFactory { SimpleObjectProperty(it.value.lastRecurrence) }
        lastRecurrenceColumn.setCellFactory {
            object : TableCell<RecurringEntry, LocalDate>() {

                override fun updateItem(item: LocalDate?, empty: Boolean) {

                    super.updateItem(item, empty)

                    UIUtils.formatDate(this, item, empty)
                }
            }
        }

        recurringEntryTable.columns.add(lastRecurrenceColumn)

        val startOfRecurrenceColumn = TableColumn<RecurringEntry, LocalDate>("Start of Recurrence")
        startOfRecurrenceColumn.prefWidth = UIConstants.RECURRENCE_DATE_WIDTH
        startOfRecurrenceColumn.setCellValueFactory { SimpleObjectProperty(it.value.startOfRecurrence) }
        startOfRecurrenceColumn.setCellFactory {
            object : TableCell<RecurringEntry, LocalDate>() {

                override fun updateItem(item: LocalDate?, empty: Boolean) {

                    super.updateItem(item, empty)

                    UIUtils.formatDate(this, item, empty)
                }
            }
        }

        recurringEntryTable.columns.add(startOfRecurrenceColumn)

        recurringEntryTable.isFocusTraversable = false
    }

    private fun prepareEntryTable() {

        entryTable.columnResizePolicy = Callback { true }

        val dateColumn = TableColumn<Entry, LocalDate>("Date")
        dateColumn.prefWidth = UIConstants.DATE_WIDTH
        dateColumn.setCellValueFactory { SimpleObjectProperty(it.value.entryDate) }
        dateColumn.setCellFactory {
            object : TableCell<Entry, LocalDate>() {

                override fun updateItem(item: LocalDate?, empty: Boolean) {

                    super.updateItem(item, empty)

                    UIUtils.formatDate(this, item, empty)
                }
            }
        }

        entryTable.columns.add(dateColumn)

        val sequenceColumn = TableColumn<Entry, EntryOrder>("")
        sequenceColumn.prefWidth = UIConstants.SEQUENCE_WIDTH
        sequenceColumn.styleClass.add(UIConstants.ALIGN_CENTER)
        sequenceColumn.setCellValueFactory { SimpleObjectProperty(it.value.entryOrder) }
        sequenceColumn.setCellFactory {
            object : TableCell<Entry, EntryOrder>() {

                override fun updateItem(item: EntryOrder?, empty: Boolean) {
                    super.updateItem(item, empty)

                    graphic = if (item == null || empty) {
                        null
                    } else {
                        when (item.sequence) {
                            EntrySequence.FIRST -> UIUtils.getIcon(FontAwesomeIcon.CHEVRON_CIRCLE_UP)
                            EntrySequence.LAST -> UIUtils.getIcon(FontAwesomeIcon.CHEVRON_CIRCLE_DOWN)
                            else -> null
                        }
                    }
                }
            }
        }

        entryTable.columns.add(sequenceColumn)

        val amountColumn = TableColumn<Entry, BigDecimal>("Amount")
        amountColumn.prefWidth = UIConstants.AMOUNT_WIDTH
        amountColumn.setCellValueFactory { SimpleObjectProperty(it.value.amount) }
        amountColumn.setCellFactory {
            object : TableCell<Entry, BigDecimal>() {

                override fun updateItem(item: BigDecimal?, empty: Boolean) {

                    super.updateItem(item, empty)

                    UIUtils.formatAmount(this, item, empty)
                }
            }
        }

        entryTable.columns.add(amountColumn)

        val savingColumn = TableColumn<Entry, Boolean>("Saving")
        savingColumn.prefWidth = UIConstants.SAVING_WIDTH
        savingColumn.setCellValueFactory { SimpleBooleanProperty(it.value.isSaving) }
        savingColumn.setCellFactory { de.omilke.bankingfx.report.categories.control.SavingTableCell() }
        entryTable.columns.add(savingColumn)

        val categoryColumn = TableColumn<Entry, String>("Category")
        categoryColumn.prefWidth = UIConstants.CATEGORY_WIDTH
        categoryColumn.setCellValueFactory { SimpleObjectProperty(it.value.category) }
        entryTable.columns.add(categoryColumn)

        val commentColumn = TableColumn<Entry, String>("Comment")
        commentColumn.prefWidth = UIConstants.COMMENT_WIDTH
        commentColumn.setCellValueFactory { SimpleObjectProperty(it.value.comment) }
        entryTable.columns.add(commentColumn)

        entryTable.isFocusTraversable = false

        entryTable.items.addListener(ListChangeListener {

            entryCountLabel.text = it.list.size.toString()

        })
    }

    private fun setupDateFilter() {

        fromDate.valueProperty().addListener { _, _, newDate ->

            toDate.value = LocalDate.of(newDate.year, newDate.month, newDate.lengthOfMonth())
            generateRecurrences(false)
        }

        val now = LocalDate.now()
        fromDate.value = LocalDate.of(now.year, now.month, 1)
    }

    @FXML
    fun regenerate() {

        generateRecurrences(true)
    }

    private fun generateRecurrences(regenerate: Boolean) {

        entryTable.items.clear()

        val items = ArrayList(recurringEntryTable.items)
        for (recurringEntry in items) {

            entryTable.items.addAll(recurringEntry.generateRecurrenceFor(fromDate.value, toDate.value, regenerate))
        }

        // TODO fix workaround?
        //workaround absent property binding in order to enable updating last recurrence date after generation
        //essentially correlates with how add JavaFX properties to (DDD) entities (esp. when using kotlin data classes, which are sealed)
        recurringEntryTable.items.clear()
        recurringEntryTable.items.addAll(items)
    }

    @FXML
    fun saveToFile() {

        val fileChooser = DefaultFileChooser("regelbuchungen.csv", entryTable.scene.window, "Select Save File", FileChooser.ExtensionFilter("CSV files", "*.csv"))

        val result = fileChooser.showSave()

        if (result.isPresent) {

            LinesWriter.saveLinesToFile(result.get(), exportConverted(entryTable.items))
        }
    }

    private fun exportConverted(entriesToExport: List<Entry>): List<String> {

        val formatter = JHaushaltFormatter(true)

        return entriesToExport
                .stream()
                .map(formatter::format)
                .collect(Collectors.toList())

    }

}