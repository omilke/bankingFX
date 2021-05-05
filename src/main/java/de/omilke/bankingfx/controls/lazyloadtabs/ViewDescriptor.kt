package de.omilke.bankingfx.controls.lazyloadtabs

import de.saxsys.mvvmfx.FxmlView
import de.saxsys.mvvmfx.ViewModel
import javafx.scene.Node

class ViewDescriptor(val title: String, val viewClass: Class<out FxmlView<out ViewModel>>, val icon: Node? = null)