package de.omilke.bankingfx

import de.omilke.banking.interop.importing.Importer.Companion.doImport
import de.omilke.banking.persistence.PersistenceServiceProvider.persistenceService
import de.omilke.bankingfx.main.MainView
import de.omilke.bankingfx.resources.ImageProvider
import de.omilke.bankingfx.resources.ImageType
import de.saxsys.mvvmfx.FluentViewLoader
import javafx.application.Application
import javafx.scene.Scene
import javafx.stage.Stage

class BankingFxApplication : Application() {

    override fun start(stage: Stage) {

        val scene = Scene(FluentViewLoader.fxmlView(MainView::class.java).load().view)
        scene.stylesheets.add(UIConstants.cssUri)

        stage.isMaximized = true
        stage.title = "Banking.fx"
        stage.icons.add(ImageProvider.readImageFromMetaInf(ImageType.MAIN))
        stage.scene = scene

        stage.show()
    }

    companion object {

        @JvmStatic
        fun main(args: Array<String>) {

            persistenceService.checkPersistenceLayerReadiness()
            doImport()

            launch(BankingFxApplication::class.java, *args)
        }
    }
}