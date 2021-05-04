package de.omilke.bankingfx.controls

import javafx.scene.control.ResizeFeaturesBase
import javafx.util.Callback

/**
 * Makes a column always resizable, which does not seem to be configurable in FXML.
 */
class ResizeableColumnCallback<T : ResizeFeaturesBase<*>?> : Callback<T, Boolean> {

    override fun call(param: T): Boolean {
        return true
    }
}