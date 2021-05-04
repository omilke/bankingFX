package de.omilke.bankingfx.report.savings

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon
import de.omilke.bankingfx.UIConstants
import de.omilke.bankingfx.controls.AmountTreeTableCell
import de.omilke.bankingfx.controls.ResizeableColumnCallback
import de.omilke.bankingfx.controls.UIUtils.getIconWithColor
import de.omilke.bankingfx.report.savings.model.Category
import de.omilke.bankingfx.report.savings.model.Entry
import de.saxsys.mvvmfx.Context
import de.saxsys.mvvmfx.FxmlView
import de.saxsys.mvvmfx.InjectContext
import de.saxsys.mvvmfx.InjectViewModel
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.scene.control.*
import javafx.scene.paint.Color
import java.math.BigDecimal

class SavingsView : FxmlView<SavingsModel> {

    @FXML
    lateinit var balancedCategories: CheckBox

    @FXML
    lateinit var savingsTable: TreeTableView<Entry>

    @InjectViewModel
    private lateinit var viewModel: SavingsModel

    @InjectContext
    private lateinit var context: Context

    fun initialize() {

        savingsTable.columnResizePolicy = ResizeableColumnCallback()
        savingsTable.root = TreeItem(Entry())
        savingsTable.isShowRoot = false

        setColumns()

        displayModel(balancedCategories.isSelected)
    }

    private fun displayModel(hideBalancedCategories: Boolean) {

        savingsTable.root.children.clear()

        for (current in viewModel.categories) {

            if (displayCategory(current, hideBalancedCategories)) {

                val currentRoot = TreeItem(Entry(current.name, current.sum))
                currentRoot.isExpanded = false

                savingsTable.root.children.add(currentRoot)

                for (entry in current.entries) {
                    currentRoot.children.add(TreeItem(entry))
                }
            }
        }
    }

    private fun displayCategory(current: Category, hideBalancedCategories: Boolean): Boolean {

        return when {
            hideBalancedCategories && current.sum.compareTo(BigDecimal.ZERO) == 0 -> false
            else -> true
        }
    }

    private fun setColumns() {

        savingsTable.setRowFactory {
            object : TreeTableRow<Entry?>() {
                override fun updateItem(item: Entry?, empty: Boolean) {

                    super.updateItem(item, empty)

                    styleClass.removeAll(UIConstants.GROUP_ROW)
                    if (!empty && item != null && item.isGroupElement) {
                        styleClass.add(UIConstants.GROUP_ROW)
                    }
                }
            }
        }

        val groupLabelColumn = TreeTableColumn<Entry, String>("Category")
        groupLabelColumn.prefWidth = UIConstants.CATEGORY_WIDTH
        groupLabelColumn.styleClass.add(UIConstants.DESCRIPTION_COLUMN)
        groupLabelColumn.setCellValueFactory { param -> param.value.value!!.descriptionProperty() }

        val amountColumn = TreeTableColumn<Entry, BigDecimal>("Amount")
        amountColumn.prefWidth = UIConstants.AMOUNT_WIDTH
        amountColumn.styleClass.add(UIConstants.ALIGN_RIGHT)
        amountColumn.setCellValueFactory { param -> param.value.value!!.amountProperty() }
        amountColumn.setCellFactory { AmountTreeTableCell() }

        val savingColumn = TreeTableColumn<Entry, Boolean>("Saving")
        savingColumn.prefWidth = UIConstants.SAVING_WIDTH
        savingColumn.styleClass.add(UIConstants.ALIGN_CENTER)
        savingColumn.setCellValueFactory { param -> param.value.value!!.savingProperty() }
        savingColumn.setCellFactory {
            object : TreeTableCell<Entry?, Boolean>() {
                override fun updateItem(item: Boolean?, empty: Boolean) {

                    super.updateItem(item, empty)

                    val rowItem = treeTableRow.item
                    if (!empty && item != null && item && rowItem != null) {
                        val amount = rowItem.amountProperty().get()

                        graphic = when {
                            amount!! < BigDecimal.ZERO -> getIconWithColor(FontAwesomeIcon.DOWNLOAD, Color.GREEN)
                            else -> getIconWithColor(FontAwesomeIcon.UPLOAD, Color.RED)
                        }
                    } else {

                        graphic = null
                    }
                }
            }
        }

        val commentColumn = TreeTableColumn<Entry?, String>("Comment")
        commentColumn.prefWidth = UIConstants.COMMENT_WIDTH
        commentColumn.setCellValueFactory { param -> param.value.value!!.commentProperty() }

        savingsTable.columns.clear()
        savingsTable.columns.add(groupLabelColumn)
        savingsTable.columns.add(amountColumn)
        savingsTable.columns.add(savingColumn)
        savingsTable.columns.add(commentColumn)
    }

    @FXML
    fun updateModel(actionEvent: ActionEvent) {

        val source = actionEvent.source as CheckBox
        displayModel(source.isSelected)
    }
}