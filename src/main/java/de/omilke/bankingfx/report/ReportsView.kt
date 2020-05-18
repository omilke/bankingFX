package de.omilke.bankingfx.report

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon
import de.omilke.bankingfx.controls.extensions.getAsIconView
import de.omilke.bankingfx.controls.extensions.prepareLazilyLoadingTabs
import de.omilke.bankingfx.controls.lazyloadtabs.ViewDescriptor
import de.omilke.bankingfx.report.audit.AuditView
import de.omilke.bankingfx.report.balance.BalanceView
import de.omilke.bankingfx.report.categories.CategoriesView
import de.omilke.bankingfx.report.fortunehistory.FortunehistoryView
import de.omilke.bankingfx.report.savings.SavingsView
import de.saxsys.mvvmfx.FxmlView
import javafx.fxml.FXML
import javafx.scene.control.TabPane


class ReportsView : FxmlView<ReportsModel> {

    private val views: Map<String, ViewDescriptor> by lazy {

        LinkedHashMap<String, ViewDescriptor>().apply {

            put("Categories", ViewDescriptor("Categories", CategoriesView::class.java, FontAwesomeIcon.TABLE.getAsIconView()))
            put("Savings", ViewDescriptor("Savings", SavingsView::class.java, FontAwesomeIcon.STAR.getAsIconView()))
            put("Balance", ViewDescriptor("Balance", BalanceView::class.java, FontAwesomeIcon.BALANCE_SCALE.getAsIconView()))
            put("Fortune History", ViewDescriptor("Fortune History", FortunehistoryView::class.java, FontAwesomeIcon.LINE_CHART.getAsIconView()))
            put("Audit", ViewDescriptor("Audit", AuditView::class.java, FontAwesomeIcon.SEARCH_PLUS.getAsIconView()))
        }
    }

    @FXML
    lateinit var reportsTabPane: TabPane

    fun initialize() {

        reportsTabPane.prepareLazilyLoadingTabs(views)
    }
}


