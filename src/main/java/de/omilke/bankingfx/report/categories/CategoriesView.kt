package de.omilke.bankingfx.report.categories

import de.omilke.bankingfx.UIConstants
import de.omilke.bankingfx.controls.extensions.atStartOfMonth
import de.omilke.bankingfx.report.categories.control.AmountContextMenuTableCell
import de.omilke.bankingfx.report.categories.control.CategoryConverter
import de.omilke.bankingfx.report.categories.model.CategoryOverTimeElement
import de.omilke.bankingfx.report.categories.model.CategoryReportModel
import de.omilke.bankingfx.report.categories.service.FetchEntriesAndBuildModelTask
import de.omilke.bankingfx.service.BackgroundProcessAndUpdateUI
import de.omilke.util.DurationProvider
import de.saxsys.mvvmfx.FxmlView
import javafx.beans.property.SimpleObjectProperty
import javafx.fxml.FXML
import javafx.scene.control.*
import org.apache.logging.log4j.Level
import org.apache.logging.log4j.LogManager
import java.math.BigDecimal
import java.time.LocalDate
import java.time.YearMonth
import java.util.*

class CategoriesView : FxmlView<CategoriesModel> {

    @FXML
    private lateinit var categoryTable: TableView<CategoryOverTimeElement>

    @FXML
    private lateinit var fromDate: DatePicker

    @FXML
    private lateinit var toDate: DatePicker

    @FXML
    private lateinit var selectedCategories: ChoiceBox<String>

    @FXML
    private lateinit var progressBar: ProgressBar


    fun initialize() {

        setupControls()

        fillTableFromDefaultSetting()
    }

    private fun setupControls() {

        this.categoryTable.selectionModel.selectionMode = SelectionMode.MULTIPLE

        fromDate.valueProperty().addListener { _, _, _ -> updateTableModel() }
        toDate.valueProperty().addListener { _, _, _ -> updateTableModel() }

        selectedCategories.converter = CategoryConverter()
        selectedCategories.valueProperty().addListener { _, _, _ -> updateTableModel() }
    }

    private fun fillTableFromDefaultSetting() {

        //once the listener is active setting the value will trigger the update
        fromDate.value = defaultFromValue
    }

    private fun updateTableModel() {

        progressBar.progress = ProgressBar.INDETERMINATE_PROGRESS

        val nanoTime = System.nanoTime()

        //clear them upfront to enable a significant visual clue on when the process is finished
        categoryTable.items.clear()
        categoryTable.columns.clear()

        LOGGER.log(Level.INFO, "Clearing table took {}", DurationProvider.formatDurationSince(nanoTime))

        val task = FetchEntriesAndBuildModelTask(fromDate.value, toDate.value, selectedCategories.value)
        BackgroundProcessAndUpdateUI(task, ::updateViewRunnable).process()

        LOGGER.log(Level.INFO, "Leaving show() after {}", DurationProvider.formatDurationSince(nanoTime))
    }

    private fun updateViewRunnable(model: CategoryReportModel): Runnable {

        return UpdateCategoriesViewRunnable(model)
    }

    private inner class UpdateCategoriesViewRunnable(private val model: CategoryReportModel) : Runnable {

        override fun run() {

            val nanoTime = System.nanoTime()

            setupHeaderColumn()
            setupDataColumns(model.yearMonthColumns)

            //this seems to be slow. Could be accelerated when the node is laid out in the background (i. e. the TableView is not attached to the scenegraph) and only afterwards added to the scenegraph.
            categoryTable.items.setAll(model.getModelData())

            if (selectedCategories.value == null) {
                //only renew categories when no category filter is set, thus retaining the list of categories to allow
                //switching to different categories without the need to reset the filter first.

                with(selectedCategories) {
                    items.clear()

                    //representation of null value governed in CategoryConverter
                    items.add(null)
                    items.addAll(model.categoriesWithExpenditure)
                }
            }

            progressBar.progress = 0.0

            LOGGER.log(Level.INFO, "Rebuilding table with category sums took {}", DurationProvider.formatDurationSince(nanoTime))
        }

        private fun setupHeaderColumn() {

            val headerColumn = TableColumn<CategoryOverTimeElement, String>("Category")
            headerColumn.styleClass += UIConstants.ALIGN_LEFT
            headerColumn.setCellValueFactory { it.value.categoryNameProperty() }

            categoryTable.columns += headerColumn
        }

        private fun setupDataColumns(columns: SortedSet<YearMonth>) {

            //add all available month column to the TableView
            for ((i, currentMonth) in columns.withIndex()) {

                val monthColumn = TableColumn<CategoryOverTimeElement, BigDecimal>(currentMonth.format(UIConstants.MONTH_FORMATTER))
                monthColumn.prefWidth = UIConstants.AMOUNT_WIDTH
                monthColumn.setCellFactory { AmountContextMenuTableCell(currentMonth) }

                monthColumn.setCellValueFactory {

                    SimpleObjectProperty(it.value.monthValues[i])
                }

                categoryTable.columns += monthColumn
            }

            //add average / sum column
            val sumColumn = TableColumn<CategoryOverTimeElement, BigDecimal>("Sum")
            sumColumn.prefWidth = UIConstants.AMOUNT_WIDTH
            sumColumn.setCellValueFactory { it.value.sumProperty() }
            sumColumn.setCellFactory { AmountContextMenuTableCell(columns.first(), columns.last()) }
            categoryTable.columns += sumColumn

            val averageColumn = TableColumn<CategoryOverTimeElement, BigDecimal>("Average")
            averageColumn.prefWidth = UIConstants.AMOUNT_WIDTH
            averageColumn.setCellValueFactory { it.value.averageProperty() }
            averageColumn.setCellFactory { AmountContextMenuTableCell() }
            categoryTable.columns += averageColumn
        }
    }

    companion object {

        private val LOGGER = LogManager.getLogger(CategoriesView::class.java)

        private const val INCLUDED_PAST_MONTH = 12L
        private val defaultFromValue: LocalDate = YearMonth.now().minusMonths(INCLUDED_PAST_MONTH).atStartOfMonth()

    }
}