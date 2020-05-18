package de.omilke.bankingfx.controls.lazyloadtabs

import de.saxsys.mvvmfx.FluentViewLoader.fxmlView
import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue
import javafx.scene.control.Tab
import org.apache.logging.log4j.Level
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

class LazyTabLoadListener(private val views: Map<String, ViewDescriptor>) : ChangeListener<Tab> {

    override fun changed(property: ObservableValue<out Tab>, oldTab: Tab?, selectedTab: Tab) {

        if (selectedTab.content == null) {
            val currentTitle = selectedTab.text
            LOGGER.log(Level.INFO, "Lazily loading content for Tab '{}'", currentTitle)

            val correspondingViewClass = views[currentTitle]!!.viewClass
            selectedTab.content = fxmlView(correspondingViewClass).load().view
        }
    }

    companion object {

        val LOGGER: Logger = LogManager.getLogger(LazyTabLoadListener::class.java)
    }

}