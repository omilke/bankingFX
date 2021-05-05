package de.omilke.bankingfx.controls.extensions

import de.omilke.bankingfx.controls.lazyloadtabs.LazyTabLoadListener
import de.omilke.bankingfx.controls.lazyloadtabs.ViewDescriptor
import javafx.scene.control.Tab
import javafx.scene.control.TabPane

fun TabPane.prepareLazilyLoadingTabs(views: Map<String, ViewDescriptor>) {

    for (currentView in views) {

        val descriptor = currentView.value

        val newTab = Tab(descriptor.title).apply {
            graphic = descriptor.icon
        }

        this.tabs.add(newTab)
    }

    this.selectionModel.clearSelection()
    this.selectionModel.selectedItemProperty().addListener(LazyTabLoadListener(views))
    this.selectionModel.selectFirst()
}