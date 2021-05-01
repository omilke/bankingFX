package de.omilke.bankingfx.controls

import de.omilke.bankingfx.UIConstants
import javafx.scene.control.TreeTableCell

open class PositiveNegativeTableCell<T, X>(
        private val isPositive: (X) -> Boolean,
        private val format: (X) -> String
) : TreeTableCell<T, X>() {

    override fun updateItem(item: X, empty: Boolean) {

        super.updateItem(item, empty)

        //reset style in any case to prevent potentially inconsistent styles
        styleClass.removeAll(UIConstants.ALIGN_RIGHT, UIConstants.POSITIVE, UIConstants.NEGATIVE)

        if (empty || item == null) {
            setText(null)
        } else {

            //TODO: make more generic with a list of composable transformations, which could also include unary conditional style application

            styleClass.add(UIConstants.ALIGN_RIGHT)

            if (isPositive(item)) {
                styleClass.add(UIConstants.POSITIVE)
            } else {
                styleClass.add(UIConstants.NEGATIVE)
            }

            setText(format(item))
        }
    }
}