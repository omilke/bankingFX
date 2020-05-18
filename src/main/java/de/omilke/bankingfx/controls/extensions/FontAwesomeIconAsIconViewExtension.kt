package de.omilke.bankingfx.controls.extensions

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView
import javafx.scene.text.Text

fun FontAwesomeIcon.getAsIconView(): Text {

    return FontAwesomeIconView(this)

}