package de.omilke.bankingfx.controls

import de.omilke.banking.BankingConfigurator
import de.omilke.banking.ConfigurationConstants
import javafx.stage.FileChooser
import javafx.stage.Window
import java.io.File

class DefaultFileChooser(initialFileName: String, private val window: Window, title: String, extensionFilter: FileChooser.ExtensionFilter) {

    private val fileChooser = FileChooser()

    init {
        fileChooser.title = title

        DEFAULT_LOCATION?.let {
            fileChooser.initialDirectory = File(it)
        }

        fileChooser.extensionFilters += extensionFilter
        fileChooser.selectedExtensionFilter = extensionFilter
        fileChooser.initialFileName = initialFileName
    }

    fun showSave(): File? {

        return fileChooser.showSaveDialog(window)
    }

    fun showOpen(): File? {

        return fileChooser.showOpenDialog(window)
    }

    companion object {

        private val DEFAULT_LOCATION = BankingConfigurator.findPropertyConfiguration(ConfigurationConstants.IMPORT_DIALOG_PATH)
    }
}