package de.omilke.bankingfx.controls

import de.omilke.banking.account.entity.Entry
import de.omilke.banking.persistence.PersistenceServiceProvider
import de.omilke.bankingfx.UIConstants
import de.omilke.bankingfx.controls.extensions.atStartOfMonth
import de.omilke.bankingfx.report.categories.model.MatchingEntryPair
import javafx.beans.property.SimpleObjectProperty
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.control.*
import javafx.scene.layout.Region
import javafx.scene.layout.VBox
import javafx.util.Callback
import org.controlsfx.control.PopOver
import java.io.IOException
import java.math.BigDecimal
import java.time.LocalDate
import java.time.YearMonth


class CompareWithPreviousMonthPopover(month: YearMonth, category: String?) : PopOver(VBox()) {

    @FXML
    private lateinit var entrypairTable: TableView<MatchingEntryPair>

    @FXML
    private lateinit var entryCountLabelFirstMonth: Label

    @FXML
    private lateinit var entryCountLabelSecondMonth: Label

    @FXML
    private lateinit var entrySumLabelFirstMonth: Label

    @FXML
    private lateinit var entrySumLabelSecondMonth: Label

    private val entryRepository = PersistenceServiceProvider.persistenceService.entryRepository

    init {
        loadFxml()

        val previousMonth = month.minusMonths(1)
        setupPopover(previousMonth, month, category)

        prepareEntryTable()

        val entriesFirstMonth = entryRepository
                .findAllEntriesBetweenWithCategoryName(
                        previousMonth.atStartOfMonth(),
                        previousMonth.atEndOfMonth(),
                        category)
        val entriesSecondMonth = entryRepository
                .findAllEntriesBetweenWithCategoryName(
                        month.atStartOfMonth(),
                        month.atEndOfMonth(),
                        category)

        setupLabels(entriesFirstMonth, entriesSecondMonth)

        setupTables(matchEntries(entriesFirstMonth, entriesSecondMonth))
    }

    //TODO: this actually is domain logic
    private fun matchEntries(entriesInEarlierMonth: List<Entry>, entriesOfLaterMonth: List<Entry>): MutableList<MatchingEntryPair> {

        val matchedEntries: MutableList<MatchingEntryPair> = ArrayList()

        val leftOverEntriesFromEarlierMonth = ArrayList(entriesInEarlierMonth)

        for (entry in entriesOfLaterMonth) {

            //compare to left-over entries to allow matching multiple entries with the same comment per month (e. g. weekly expenses)
            val matchedEntry = findMatch(entry, leftOverEntriesFromEarlierMonth)
            matchedEntries.add(MatchingEntryPair(matchedEntry, entry))
            leftOverEntriesFromEarlierMonth.remove(matchedEntry)
        }

        for (entry in leftOverEntriesFromEarlierMonth) {
            //these have not been matched, meaning there can be no match in later month because matching is mutual
            //therefore they have to be listed without matching entry of later month
            matchedEntries.add(MatchingEntryPair(entry, null))
        }

        //sort with natural order, defined by MatchingEntryPair
        matchedEntries.sort()
        return matchedEntries
    }

    private fun findMatch(referenceEntry: Entry, otherEntries: List<Entry>): Entry? {

        for (otherEntry in otherEntries) {

            if (isMatchForMonthComparison(referenceEntry, otherEntry)) {
                return otherEntry
            }
        }

        return null
    }

    private fun isMatchForMonthComparison(referenceEntry: Entry, otherEntry: Entry): Boolean {

        //for the sake of comparing expensing, the defines the same intention
        //differences in amount, category or date can therefore be visualized accordingly
        return referenceEntry.comment == otherEntry.comment
    }

    private fun setupTables(matchedEntries: MutableList<MatchingEntryPair>) {

        entrypairTable.items.addAll(matchedEntries)
    }

    private fun loadFxml() {

        val fxmlLoader = FXMLLoader(javaClass.getResource("CompareWithPreviousMonthPopover.fxml"))

        fxmlLoader.setRoot(this.contentNode)
        fxmlLoader.setController(this)

        try {
            fxmlLoader.load<Nothing>()
        } catch (exception: IOException) {
            throw RuntimeException(exception)
        }
    }

    private fun setupPopover(first: YearMonth, second: YearMonth, selectedCategory: String?) {

        title = buildPopOverTitle(first, second, selectedCategory)

        arrowSize = 1.0
        cornerRadius = 10.0
        isAnimated = true
        isHeaderAlwaysVisible = true
        isHideOnEscape = true

        prefWidth(Region.USE_COMPUTED_SIZE)
        prefHeight(Region.USE_COMPUTED_SIZE)
    }

    private fun buildPopOverTitle(first: YearMonth, last: YearMonth, selectedCategory: String?): String {

        val month = "${formatMonthColumnHeader(first)} compared to ${formatMonthColumnHeader(last)}"

        return if (selectedCategory == null) {
            month
        } else {
            "$month [$selectedCategory]"
        }
    }

    private fun formatMonthColumnHeader(period: YearMonth): String {

        return period.format(UIConstants.MONTH_FORMATTER)
    }

    private fun prepareEntryTable() {

        entrypairTable.columnResizePolicy = Callback { true }

        entrypairTable.styleClass.add("comparison-table")

        entrypairTable.setRowFactory {
            object : TableRow<MatchingEntryPair?>() {
                override fun updateItem(item: MatchingEntryPair?, empty: Boolean) {
                    super.updateItem(item, empty)

                    if (item == null || !item.isMatch()) {
                        this.styleClass.remove("matching-entry")
                    } else {
                        this.styleClass.add("matching-entry")
                    }
                }
            }
        }

        val categoryColumn = TableColumn<MatchingEntryPair, String>("Category")
        categoryColumn.prefWidth = UIConstants.CATEGORY_WIDTH
        categoryColumn.setCellValueFactory { param -> SimpleObjectProperty(param.value.earlier?.category) }
        entrypairTable.columns.add(categoryColumn)

        val amountColumn = TableColumn<MatchingEntryPair, BigDecimal>("Amount")
        amountColumn.prefWidth = UIConstants.AMOUNT_WIDTH
        amountColumn.setCellValueFactory { SimpleObjectProperty(it.value.earlier?.amount) }
        amountColumn.setCellFactory {
            object : TableCell<MatchingEntryPair, BigDecimal>() {

                override fun updateItem(item: BigDecimal?, empty: Boolean) {

                    super.updateItem(item, empty)

                    UIUtils.formatAmount(this, item, empty)

                    UIUtils.applyStyleClass(this, item, empty, tableRow.item::amountDiffers, "entry-difference")
                }
            }
        }

        entrypairTable.columns.add(amountColumn)

        val dateColumn = TableColumn<MatchingEntryPair, LocalDate>("Date")
        dateColumn.prefWidth = UIConstants.DATE_WIDTH
        dateColumn.setCellValueFactory { SimpleObjectProperty(it.value.earlier?.entryDate) }
        dateColumn.setCellFactory {
            object : TableCell<MatchingEntryPair, LocalDate>() {

                override fun updateItem(item: LocalDate?, empty: Boolean) {

                    super.updateItem(item, empty)

                    UIUtils.formatDate(this, item, empty)

                    UIUtils.applyStyleClass(this, item, empty, tableRow.item::dayOfMonthDiffers, "entry-difference")
                }
            }
        }

        entrypairTable.columns.add(dateColumn)


        val commentColumn = TableColumn<MatchingEntryPair, String>("Comment")
        commentColumn.prefWidth = UIConstants.COMMENT_WIDTH
        commentColumn.setCellValueFactory { param -> SimpleObjectProperty(param.value.getReferenceEntry().comment) }
        entrypairTable.columns.add(commentColumn)


        val dateColumnLaterEntry = TableColumn<MatchingEntryPair, LocalDate>("Date")
        dateColumnLaterEntry.prefWidth = UIConstants.DATE_WIDTH
        dateColumnLaterEntry.setCellValueFactory { SimpleObjectProperty(it.value.later?.entryDate) }
        dateColumnLaterEntry.setCellFactory {
            object : TableCell<MatchingEntryPair, LocalDate>() {

                override fun updateItem(item: LocalDate?, empty: Boolean) {

                    super.updateItem(item, empty)

                    UIUtils.formatDate(this, item, empty)

                    UIUtils.applyStyleClass(this, item, empty, tableRow.item::dayOfMonthDiffers, "entry-difference")
                }
            }
        }

        entrypairTable.columns.add(dateColumnLaterEntry)

        val amountColumnLaterEntry = TableColumn<MatchingEntryPair, BigDecimal>("Amount")
        amountColumnLaterEntry.prefWidth = UIConstants.AMOUNT_WIDTH
        amountColumnLaterEntry.setCellValueFactory { SimpleObjectProperty(it.value.later?.amount) }
        amountColumnLaterEntry.setCellFactory {
            object : TableCell<MatchingEntryPair, BigDecimal>() {

                override fun updateItem(item: BigDecimal?, empty: Boolean) {

                    super.updateItem(item, empty)

                    UIUtils.formatAmount(this, item, empty)

                    UIUtils.applyStyleClass(this, item, empty, tableRow.item::amountDiffers, "entry-difference")
                }
            }
        }

        entrypairTable.columns.add(amountColumnLaterEntry)

        val categoryColumnLaterEntry = TableColumn<MatchingEntryPair, String>("Category")
        categoryColumnLaterEntry.prefWidth = UIConstants.CATEGORY_WIDTH
        categoryColumnLaterEntry.setCellValueFactory { param -> SimpleObjectProperty(param.value.later?.category) }
        entrypairTable.columns.add(categoryColumnLaterEntry)

        entrypairTable.isFocusTraversable = false
    }

    private fun setupLabels(entriesEarlierMonth: List<Entry>, entriesLaterMonth: List<Entry>) {

        val entryCountEarlierMonth = entriesEarlierMonth.size
        val entrySumEarlierMonth = getEntrySum(entriesEarlierMonth)

        val entryCountLaterMonth = entriesLaterMonth.size
        val entrySumLaterMonth = getEntrySum(entriesLaterMonth)

        entryCountLabelFirstMonth.text = entryCountEarlierMonth.toString()
        entryCountLabelSecondMonth.text = entryCountLaterMonth.toString()

        UIUtils.formatAmount(entrySumLabelFirstMonth, entrySumEarlierMonth)
        UIUtils.formatAmount(entrySumLabelSecondMonth, entrySumLaterMonth)

        val entryCountDiffers = { entryCountEarlierMonth != entryCountLaterMonth }
        UIUtils.applyStyleClass(entryCountLabelFirstMonth, entryCountDiffers, "entry-difference")
        UIUtils.applyStyleClass(entryCountLabelSecondMonth, entryCountDiffers, "entry-difference")

        val entrySumDiffers = { entrySumEarlierMonth != entrySumLaterMonth }
        UIUtils.applyStyleClass(entrySumLabelFirstMonth, entrySumDiffers, "entry-difference")
        UIUtils.applyStyleClass(entrySumLabelSecondMonth, entrySumDiffers, "entry-difference")
    }

    private fun getEntrySum(entries: List<Entry>): BigDecimal {

        return entries.sumOf(Entry::amount)
    }

}