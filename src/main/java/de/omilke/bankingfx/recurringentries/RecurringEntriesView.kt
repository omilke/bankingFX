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
import de.omilke.bankingfx.controls.ResizeableColumnCallback
import de.omilke.bankingfx.controls.UIUtils
import de.omilke.bankingfx.main.entrylist.model.EntryOrder
import de.omilke.bankingfx.main.entrylist.model.buildEntryOrder
import de.omilke.bankingfx.recurringentries.control.SavingTableCell
import de.saxsys.mvvmfx.Context
import de.saxsys.mvvmfx.FxmlView
import de.saxsys.mvvmfx.InjectContext
import de.saxsys.mvvmfx.InjectViewModel
import javafx.beans.binding.Bindings
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.fxml.FXML
import javafx.scene.control.*
import javafx.stage.FileChooser
import java.math.BigDecimal
import java.time.LocalDate

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

        entryCountLabel.textProperty().bind(Bindings.size(entryTable.items).asString())

        val recurringEntries = rer.findAllRecurringEntries()
        recurringEntryTable.items.addAll(recurringEntries)

        setupDateFilter()

        //TODO allow saving of recurring entries --> last recurrence
        //TODO allow saving of entries
    }

    private fun initRecurringEntryTable() {

        recurringEntryTable.columnResizePolicy = ResizeableColumnCallback()
        recurringEntryTable.isFocusTraversable = false

        TableColumn<RecurringEntry, EntrySequence>("").apply {
            prefWidth = UIConstants.SEQUENCE_WIDTH
            styleClass.add(UIConstants.ALIGN_CENTER)
            setCellValueFactory { SimpleObjectProperty(it.value.sequence) }
            setCellFactory {
                object : TableCell<RecurringEntry, EntrySequence>() {

                    override fun updateItem(item: EntrySequence?, empty: Boolean) {

                        super.updateItem(item, empty)

                        graphic = when {
                            empty || item == null -> null
                            else -> when (item) {
                                EntrySequence.FIRST -> UIUtils.getIcon(FontAwesomeIcon.CHEVRON_CIRCLE_UP)
                                EntrySequence.LAST -> UIUtils.getIcon(FontAwesomeIcon.CHEVRON_CIRCLE_DOWN)
                                else -> null
                            }
                        }
                    }
                }
            }
        }.also {
            recurringEntryTable.columns.add(it)
        }

        TableColumn<RecurringEntry, Int>("Order").apply {
            prefWidth = UIConstants.SEQUENCE_WIDTH
            styleClass.add(UIConstants.ALIGN_CENTER)
            setCellValueFactory { SimpleObjectProperty(it.value.orderIndex) }
        }.also {
            recurringEntryTable.columns.add(it)
        }

        TableColumn<RecurringEntry, BigDecimal>("Amount").apply {
            prefWidth = UIConstants.AMOUNT_WIDTH
            setCellValueFactory { SimpleObjectProperty(it.value.amount) }
            setCellFactory {
                object : TableCell<RecurringEntry, BigDecimal>() {

                    override fun updateItem(item: BigDecimal?, empty: Boolean) {

                        super.updateItem(item, empty)

                        UIUtils.formatAmount(this, item, empty)
                    }
                }
            }
        }.also {
            recurringEntryTable.columns.add(it)
        }

        TableColumn<RecurringEntry, Boolean>("Saving").apply {
            prefWidth = UIConstants.SAVING_WIDTH
            setCellValueFactory { SimpleBooleanProperty(it.value.isSaving) }
            setCellFactory { SavingTableCell() }
        }.also {
            recurringEntryTable.columns.add(it)
        }

        TableColumn<RecurringEntry, String>("Category").apply {
            prefWidth = UIConstants.CATEGORY_COMPACT_WIDTH
            setCellValueFactory { SimpleObjectProperty(it.value.category) }
        }.also {
            recurringEntryTable.columns.add(it)
        }

        TableColumn<RecurringEntry, String>("Comment").apply {
            prefWidth = UIConstants.COMMENT_COMPACT_WIDTH
            setCellValueFactory { SimpleObjectProperty(it.value.comment) }
        }.also {
            recurringEntryTable.columns.add(it)
        }

        TableColumn<RecurringEntry, String>("Recurrence").apply {
            prefWidth = UIConstants.RECURRENCE_WIDTH
            styleClass.add(UIConstants.ALIGN_CENTER)
            setCellValueFactory { SimpleObjectProperty(UIUtils.formatRecurrenceStrategy(it.value.recurrenceStrategy)) }
        }.also {
            recurringEntryTable.columns.add(it)
        }

        TableColumn<RecurringEntry, LocalDate>("Last Recurrence").apply {
            prefWidth = UIConstants.RECURRENCE_DATE_WIDTH
            setCellValueFactory { SimpleObjectProperty(it.value.lastRecurrence) }
            setCellFactory {
                object : TableCell<RecurringEntry, LocalDate>() {

                    override fun updateItem(item: LocalDate?, empty: Boolean) {

                        super.updateItem(item, empty)

                        UIUtils.formatDate(this, item, empty)
                    }
                }
            }
        }.also {
            recurringEntryTable.columns.add(it)
        }

        TableColumn<RecurringEntry, LocalDate>("Start of Recurrence").apply {
            prefWidth = UIConstants.RECURRENCE_DATE_WIDTH
            setCellValueFactory { SimpleObjectProperty(it.value.startOfRecurrence) }
            setCellFactory {
                object : TableCell<RecurringEntry, LocalDate>() {

                    override fun updateItem(item: LocalDate?, empty: Boolean) {

                        super.updateItem(item, empty)

                        UIUtils.formatDate(this, item, empty)
                    }
                }
            }
        }.also {
            recurringEntryTable.columns.add(it)
        }
    }

    private fun prepareEntryTable() {

        entryTable.columnResizePolicy = ResizeableColumnCallback()
        entryTable.isFocusTraversable = false

        TableColumn<Entry, LocalDate>("Date").apply {
            prefWidth = UIConstants.DATE_WIDTH
            setCellValueFactory { SimpleObjectProperty(it.value.entryDate) }
            setCellFactory {
                object : TableCell<Entry, LocalDate>() {

                    override fun updateItem(item: LocalDate?, empty: Boolean) {

                        super.updateItem(item, empty)

                        UIUtils.formatDate(this, item, empty)
                    }
                }
            }
        }.also {
            entryTable.columns.add(it)
        }

        TableColumn<Entry, EntryOrder>("").apply {
            prefWidth = UIConstants.SEQUENCE_WIDTH
            styleClass.add(UIConstants.ALIGN_CENTER)
            setCellValueFactory { SimpleObjectProperty(it.value.buildEntryOrder()) }
            setCellFactory {
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
        }.also {
            entryTable.columns.add(it)

        }

        TableColumn<Entry, BigDecimal>("Amount").apply {
            prefWidth = UIConstants.AMOUNT_WIDTH
            setCellValueFactory { SimpleObjectProperty(it.value.amount) }
            setCellFactory {
                object : TableCell<Entry, BigDecimal>() {

                    override fun updateItem(item: BigDecimal?, empty: Boolean) {

                        super.updateItem(item, empty)

                        UIUtils.formatAmount(this, item, empty)
                    }
                }
            }
        }.also {
            entryTable.columns.add(it)

        }

        TableColumn<Entry, Boolean>("Saving").apply {
            prefWidth = UIConstants.SAVING_WIDTH
            setCellValueFactory { SimpleBooleanProperty(it.value.isSaving) }
            setCellFactory { de.omilke.bankingfx.report.categories.control.SavingTableCell() }
        }.also {
            entryTable.columns.add(it)
        }

        TableColumn<Entry, String>("Category").apply {
            prefWidth = UIConstants.CATEGORY_WIDTH
            setCellValueFactory { SimpleObjectProperty(it.value.category) }
        }.also {
            entryTable.columns.add(it)
        }

        TableColumn<Entry, String>("Comment").apply {
            prefWidth = UIConstants.COMMENT_WIDTH
            setCellValueFactory { SimpleObjectProperty(it.value.comment) }
        }.also {
            entryTable.columns.add(it)
        }
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

        result?.let {
            LinesWriter.saveLinesToFile(it, exportConverted(entryTable.items))
        }

        //TODO: a tiny feedback for user experience
    }

    private fun exportConverted(entriesToExport: List<Entry>): List<String> {

        val formatter = JHaushaltFormatter(true)

        return entriesToExport
                .map(formatter::format)

    }

}