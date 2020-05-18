package de.omilke.bankingfx.main

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon
import de.omilke.bankingfx.BankingFxApplication
import de.omilke.bankingfx.controls.extensions.getAsIconView
import de.omilke.bankingfx.controls.extensions.prepareLazilyLoadingTabs
import de.omilke.bankingfx.controls.lazyloadtabs.ViewDescriptor
import de.omilke.bankingfx.main.converter.ConverterView
import de.omilke.bankingfx.main.entrylist.EntrylistView
import de.omilke.bankingfx.misc.MiscView
import de.omilke.bankingfx.recurringentries.RecurringEntriesView
import de.omilke.bankingfx.report.ReportsView
import de.omilke.bankingfx.report.audit.AuditView
import de.omilke.bankingfx.report.balance.BalanceView
import de.omilke.bankingfx.report.categories.CategoriesView
import de.omilke.bankingfx.report.fortunehistory.FortunehistoryView
import de.omilke.bankingfx.report.savings.SavingsView
import de.omilke.bankingfx.resources.ImageProvider
import de.omilke.bankingfx.resources.ImageType
import de.saxsys.mvvmfx.FluentViewLoader
import de.saxsys.mvvmfx.FxmlView
import de.saxsys.mvvmfx.ViewModel
import javafx.fxml.FXML
import javafx.scene.Scene
import javafx.scene.control.TabPane
import javafx.scene.layout.VBox
import javafx.stage.Stage
import javafx.stage.StageStyle

class MainView : FxmlView<MainModel?> {

    private val views: Map<String, ViewDescriptor> by lazy {

        LinkedHashMap<String, ViewDescriptor>().apply {

            put("All Entries", ViewDescriptor("All Entries", EntrylistView::class.java, FontAwesomeIcon.LIST.getAsIconView()))
            put("Reports", ViewDescriptor("Reports", ReportsView::class.java, FontAwesomeIcon.BAR_CHART.getAsIconView()))
            put("Misc. Views", ViewDescriptor("Misc. Views", MiscView::class.java, FontAwesomeIcon.PLUS_CIRCLE.getAsIconView()))
        }
    }

    @FXML
    lateinit var content: VBox

    @FXML
    lateinit var mainTabPane: TabPane

    fun initialize() {

        mainTabPane.prepareLazilyLoadingTabs(views)
    }

    @FXML
    fun showImport() {

/*        final FXMLView appView = new ImporterModel();
        final String title = "Import Entries";

        ImporterView importerPresenter = (ImporterView) appView.getPresenter();
        importerPresenter.setRefreshAction(this.entrylistPresenter::refreshEntries);

        openNewView(appView, title);*/
    }

    @FXML
    fun showConvert() {
        openNewView(ConverterView::class.java, "Convert Entries")
    }

    @FXML
    fun showCategories() {
        openNewView(CategoriesView::class.java, "Categories")
    }

    @FXML
    fun showSavings() {
        openNewView(SavingsView::class.java, "Savings")
    }

    @FXML
    fun showAudit() {
        openNewView(AuditView::class.java, "Balance Audit")
    }

    @FXML
    fun showBalance() {
        openNewView(BalanceView::class.java, "Monthly Balance")
    }

    @FXML
    fun showFortuneHistory() {
        openNewView(FortunehistoryView::class.java, "Fortune History")
    }

    @FXML
    fun showRecurringEntries() {
        openNewView(RecurringEntriesView::class.java, "Recurring Entries")
    }

    fun showReports() {
        openNewView(ReportsView::class.java, "Reports")
    }

    fun showMiscViews() {
        openNewView(MiscView::class.java, "Misc. Views")
    }

    private fun openNewView(clazz: Class<out FxmlView<out ViewModel>>, title: String) {

        val scene = Scene(FluentViewLoader.fxmlView(clazz).load().view)

        val uri = BankingFxApplication::class.java.getResource("bankingfx.css").toExternalForm()
        scene.stylesheets.add(uri)

        val stage = Stage()
        stage.icons.add(ImageProvider.readImageFromMetaInf(ImageType.CHART))
        stage.isResizable = true
        stage.title = title
        stage.scene = scene
        stage.initStyle(StageStyle.UNIFIED)

        stage.initOwner(content.scene.window)
        stage.show()
    }
}