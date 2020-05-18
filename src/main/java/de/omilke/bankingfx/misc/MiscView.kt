package de.omilke.bankingfx.misc

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon
import de.omilke.bankingfx.controls.extensions.getAsIconView
import de.omilke.bankingfx.controls.extensions.prepareLazilyLoadingTabs
import de.omilke.bankingfx.controls.lazyloadtabs.ViewDescriptor
import de.omilke.bankingfx.main.converter.ConverterView
import de.omilke.bankingfx.recurringentries.RecurringEntriesView
import de.saxsys.mvvmfx.FxmlView
import javafx.fxml.FXML
import javafx.scene.control.TabPane


class MiscView : FxmlView<MiscModel> {

    private val views: Map<String, ViewDescriptor> by lazy {

        LinkedHashMap<String, ViewDescriptor>().apply {

            put("Recurring Entries", ViewDescriptor("Recurring Entries", RecurringEntriesView::class.java, FontAwesomeIcon.REFRESH.getAsIconView()))
            put("Convert Entries", ViewDescriptor("Convert Entries", ConverterView::class.java, FontAwesomeIcon.REPEAT.getAsIconView()))
        }
    }

    @FXML
    lateinit var miscTabPane: TabPane

    fun initialize() {

        miscTabPane.prepareLazilyLoadingTabs(views)
    }
}


