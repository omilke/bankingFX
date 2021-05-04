package de.omilke.bankingfx.stocks.transactions

import de.omilke.banking.persistence.PersistenceServiceProvider
import de.omilke.bankingfx.UIConstants
import de.omilke.bankingfx.controls.AmountTreeTableCell
import de.omilke.bankingfx.controls.PositiveNegativeTableCell
import de.omilke.bankingfx.controls.UIUtils
import de.omilke.bankingfx.stocks.transactions.model.TransactionTableRow
import de.saxsys.mvvmfx.FxmlView
import javafx.fxml.FXML
import javafx.scene.control.TreeItem
import javafx.scene.control.TreeTableColumn
import javafx.scene.control.TreeTableRow
import javafx.scene.control.TreeTableView
import org.apache.logging.log4j.LogManager
import yahoofinance.YahooFinance
import java.math.BigDecimal

class TransactionView : FxmlView<TransactionModel> {

    private val sr = PersistenceServiceProvider.persistenceService.securityRepository

    @FXML
    private lateinit var transactionTable: TreeTableView<TransactionTableRow>

    fun initialize() {

        setupControls()

        displaySecurities()

    }

    private fun displaySecurities() {

        for (security in sr.findAllSecurity()) {

            val currentRoot = TreeItem(TransactionTableRow(security))
            currentRoot.isExpanded = true

            for (transaction in security.transactions) {
                currentRoot.children.add(TreeItem(TransactionTableRow(transaction)))
            }

            val totalInvestedAmount = security.totalInvestedAmount

            if (security.transactions.size > 1) {
                val aggregateElement = TreeItem(TransactionTableRow("Sum", security.totalCount, totalInvestedAmount))
                currentRoot.children.add(aggregateElement)
            }

            //TODO: make fetching data optional and in background
            if (!security.isClosedPosition) {

                val quote = YahooFinance.get(security.referenceTicker)?.quote
                quote?.let {
                    val aggregateElement = TreeItem(TransactionTableRow("Change", security.updateCurrentPrice(it.price)))
                    currentRoot.children.add(aggregateElement)
                }

                //TODO: include projection about sale including sell provision, if given
            }

            this.transactionTable.root.children.add(currentRoot)
        }

        //TODO: display sum of portfolio
    }

    private fun setupControls() {

        setupTable()
    }

    private fun setupTable() {

        transactionTable.setRowFactory {
            object : TreeTableRow<TransactionTableRow?>() {
                override fun updateItem(item: TransactionTableRow?, empty: Boolean) {
                    super.updateItem(item, empty)

                    if (empty || item == null) {
                        styleClass.removeAll("security-group", "transaction-aggregate")
                    } else {
                        if (item.parentElement) {
                            styleClass.add("security-group")
                        } else if (item.aggregateElement) {
                            styleClass.add("transaction-aggregate")
                        }
                    }
                }
            }
        }

        val descriptionColumn = TreeTableColumn<TransactionTableRow, String>("Security")
        descriptionColumn.prefWidth = UIConstants.SECURITY_NAME_WIDTH
        descriptionColumn.setCellValueFactory { it.value.value.description }
        descriptionColumn.styleClass.add("description")

        val countColumn = TreeTableColumn<TransactionTableRow, Int?>("Count")
        countColumn.prefWidth = UIConstants.TRANSACTION_COUNT_WIDTH
        countColumn.styleClass.add(UIConstants.ALIGN_RIGHT)
        countColumn.setCellValueFactory { it.value.value.count }

        val priceColumn = TreeTableColumn<TransactionTableRow, BigDecimal>("Price")
        priceColumn.prefWidth = UIConstants.AMOUNT_WIDTH
        priceColumn.setCellValueFactory { it.value.value.price }
        priceColumn.setCellFactory { AmountTreeTableCell() }

        val provisionColumn = TreeTableColumn<TransactionTableRow, BigDecimal>("Provision")
        provisionColumn.prefWidth = UIConstants.AMOUNT_WIDTH
        provisionColumn.setCellValueFactory { it.value.value.provision }
        provisionColumn.setCellFactory { AmountTreeTableCell() }

        val totalAmountColumn = TreeTableColumn<TransactionTableRow, BigDecimal>("Total Amount")
        totalAmountColumn.prefWidth = UIConstants.AMOUNT_WIDTH
        totalAmountColumn.setCellValueFactory { it.value.value.totalAmount }
        totalAmountColumn.setCellFactory { AmountTreeTableCell() }

        val relativeChangeColumn = TreeTableColumn<TransactionTableRow, BigDecimal>("")
        relativeChangeColumn.prefWidth = UIConstants.AMOUNT_WIDTH
        relativeChangeColumn.setCellValueFactory { it.value.value.relativeChange }
        relativeChangeColumn.setCellFactory { PositiveNegativeTableCell(UIUtils::isPositive, UIUtils::formatPercent) }

        //TODO: make table sexy

        transactionTable.columns.clear()
        transactionTable.columns.add(descriptionColumn)
        transactionTable.columns.add(countColumn)
        transactionTable.columns.add(priceColumn)
        transactionTable.columns.add(provisionColumn)
        transactionTable.columns.add(totalAmountColumn)
        transactionTable.columns.add(relativeChangeColumn)

        transactionTable.root = TreeItem(TransactionTableRow())
        transactionTable.isShowRoot = false
    }

    companion object {

        private val LOGGER = LogManager.getLogger(TransactionView::class.java)
    }
}