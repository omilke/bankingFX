package de.omilke.bankingfx.controls

import de.omilke.banking.account.entity.Entry
import de.omilke.banking.interop.exporting.LinesWriter.saveLinesToFile
import de.omilke.banking.interop.exporting.csv.CsvFormatter
import de.omilke.banking.persistence.PersistenceServiceProvider
import de.omilke.bankingfx.UIConstants
import de.omilke.bankingfx.controls.extensions.atStartOfMonth
import de.omilke.bankingfx.report.categories.control.SavingTableCell
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.control.Label
import javafx.scene.control.TableCell
import javafx.scene.control.TableColumn
import javafx.scene.control.TableView
import javafx.scene.layout.Region
import javafx.scene.layout.VBox
import javafx.stage.FileChooser
import javafx.util.Callback
import org.controlsfx.control.PopOver
import java.io.File
import java.io.IOException
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.LocalDate
import java.time.YearMonth
import java.util.*


class EntrylistPopover(private val first: YearMonth, private val last: YearMonth?, private val category: String?) : PopOver(VBox()) {

    @FXML
    private lateinit var entryTable: TableView<Entry>

    @FXML
    private lateinit var entryCountLabel: Label

    @FXML
    private lateinit var entrySumLabel: Label

    @FXML
    private lateinit var entryAverageLabel: Label

    private val entryRepository = PersistenceServiceProvider.persistenceService.entryRepository

    private val entries by lazy {

        entryRepository
            .findAllEntriesBetweenWithCategoryName(
                first.atStartOfMonth(),
                (last ?: first).atEndOfMonth(),
                category
            )
            .apply {
                sortBy { it.entryDate }
            }
    }

    init {
        loadFxml()

        setupPopover(first, last, category)

        prepareEntryTable()

        setupLabels(entries)

        entryTable.items.addAll(entries)
    }

    private fun loadFxml() {

        val fxmlLoader = FXMLLoader(javaClass.getResource("EntrylistPopover.fxml"))

        fxmlLoader.setRoot(this.contentNode)
        fxmlLoader.setController(this)

        try {
            fxmlLoader.load<Nothing>()
        } catch (exception: IOException) {
            throw RuntimeException(exception)
        }
    }

    private fun setupPopover(first: YearMonth, last: YearMonth?, selectedCategory: String?) {

        title = buildPopOverTitle(first, last, selectedCategory)

        arrowSize = 1.0
        cornerRadius = 10.0
        isAnimated = true
        isHeaderAlwaysVisible = true
        isHideOnEscape = true

        prefWidth(Region.USE_COMPUTED_SIZE)
        prefHeight(Region.USE_COMPUTED_SIZE)
    }

    private fun buildPopOverTitle(first: YearMonth, last: YearMonth?, selectedCategory: String?): String {

        val month: String = if (last == null) {
            formatMonthColumnHeader(first)
        } else {
            "${formatMonthColumnHeader(first)} - ${formatMonthColumnHeader(last)}"
        }

        return if (selectedCategory == null) {
            month
        } else {
            "$month [$selectedCategory]"
        }
    }

    private fun formatMonthColumnHeader(period: YearMonth): String {

        return period.format(UIConstants.MONTH_FORMATTER)
    }

    private fun formatMonthFileName(period: YearMonth): String {

        return period.format(UIConstants.MONTH_NAME_FORMATTER_FILENAME)
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
        savingColumn.setCellFactory { SavingTableCell() }
        entryTable.columns.add(savingColumn)

        val commentColumn = TableColumn<Entry, String>("Comment")
        commentColumn.prefWidth = UIConstants.COMMENT_WIDTH
        commentColumn.setCellValueFactory { param -> SimpleObjectProperty(param.value.comment) }
        entryTable.columns.add(commentColumn)

        val categoryColumn = TableColumn<Entry, String>("Category")
        categoryColumn.prefWidth = UIConstants.CATEGORY_WIDTH
        categoryColumn.setCellValueFactory { param -> SimpleObjectProperty(param.value.category) }
        entryTable.columns.add(categoryColumn)

        entryTable.isFocusTraversable = false
    }

    private fun setupLabels(entries: List<Entry>) {

        entryCountLabel.text = entries.size.toString()

        val sumOfEntries = getEntrySum(entries)

        UIUtils.formatAmount(entrySumLabel, sumOfEntries)
        UIUtils.formatAmount(entryAverageLabel, getEntryAverage(sumOfEntries, entries.size))
    }

    private fun getEntrySum(entries: List<Entry>): BigDecimal {

        return entries.sumOf(Entry::amount)
    }

    private fun getEntryAverage(sumOfEntries: BigDecimal, entryCount: Int): BigDecimal? {

        return if (entryCount > 0) {
            sumOfEntries.divide(BigDecimal(entryCount), 2, RoundingMode.HALF_UP)
        } else {
            BigDecimal.ZERO
        }
    }

    @FXML
    fun export() {

        val fileChooser = DefaultFileChooser(
            generateInitialFileName(),
            entryTable.scene.window,
            "Select Save File",
            FileChooser.ExtensionFilter("CSV files", "*.csv")
        )

        saveToSelectedFile(fileChooser.showSave())
    }

    private fun generateInitialFileName(): String {

        var result = formatMonthFileName(first)

        last?.let {
            result += "_${formatMonthFileName(it)}"
        }

        category?.let {
            result += "_$it"
        }

        return result
    }

    private fun saveToSelectedFile(result: File?) {

        result?.let {
            saveLinesToFile(it, formatEntries(entryTable.items))
        }
    }

    private fun formatEntries(entriesToExport: List<Entry>): List<String> {

        val formatter = CsvFormatter()

        val result: MutableList<String> = ArrayList()
        for (currentEntry in entriesToExport) {
            result.add(formatter.format(currentEntry))
        }

        return result
    }

}