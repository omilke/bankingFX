package de.omilke.bankingfx.stocks

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon
import de.omilke.bankingfx.controls.extensions.getAsIconView
import de.omilke.bankingfx.controls.extensions.prepareLazilyLoadingTabs
import de.omilke.bankingfx.controls.lazyloadtabs.ViewDescriptor
import de.omilke.bankingfx.stocks.transactions.TransactionView
import de.saxsys.mvvmfx.FxmlView
import javafx.fxml.FXML
import javafx.scene.control.TabPane


class StocksView : FxmlView<StocksModel> {

    private val views: Map<String, ViewDescriptor> by lazy {

        LinkedHashMap<String, ViewDescriptor>().apply {

            put("Transactions", ViewDescriptor("Transactions", TransactionView::class.java, FontAwesomeIcon.HANDSHAKE_ALT.getAsIconView()))
//            put("History", ViewDescriptor("Historic Growth", StockHistoryView::class.java, FontAwesomeIcon.STAR.getAsIconView()))
        }
    }

    @FXML
    lateinit var stocksTabpane: TabPane

    fun initialize() {

        stocksTabpane.prepareLazilyLoadingTabs(views)
    }
}


