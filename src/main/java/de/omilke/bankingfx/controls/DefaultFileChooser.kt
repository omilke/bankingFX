package de.omilke.bankingfx.controls

import javafx.stage.FileChooser
import javafx.stage.Window
import java.io.File
import java.util.*

class DefaultFileChooser(initialFileName: String, private val window: Window, title: String, extensionFilter: FileChooser.ExtensionFilter) {

    private val fileChooser = FileChooser()

    init {
        fileChooser.title = title
        fileChooser.initialDirectory = File(DEFAULT_LOCATION)

        fileChooser.extensionFilters += extensionFilter
        fileChooser.selectedExtensionFilter = extensionFilter
        fileChooser.initialFileName = initialFileName
    }

    fun showSave(): Optional<File> {

        return Optional.ofNullable(fileChooser.showSaveDialog(window))
    }

    fun showOpen(): Optional<File> {

        return Optional.ofNullable(fileChooser.showOpenDialog(window))
    }

    companion object {
        private const val DEFAULT_LOCATION = "D:\\~dev\\share"
    }
}