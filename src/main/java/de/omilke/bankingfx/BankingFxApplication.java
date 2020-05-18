package de.omilke.bankingfx;

import de.omilke.banking.interop.importing.Importer;
import de.omilke.banking.persistence.PersistenceServiceProvider;
import de.omilke.bankingfx.main.MainView;
import de.omilke.bankingfx.resources.ImageProvider;
import de.omilke.bankingfx.resources.ImageType;
import de.saxsys.mvvmfx.FluentViewLoader;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class BankingFxApplication extends Application {

    public static void main(String[] args) {

        PersistenceServiceProvider.INSTANCE.getPersistenceService().checkPersistenceLayerReadiness();

        Importer.Companion.doImport();

        launch(args);
    }

    @Override
    public void start(final Stage stage) {

        final Scene scene = new Scene(FluentViewLoader.fxmlView(MainView.class).load().getView());
        scene.getStylesheets().add(getClass().getResource("bankingfx.css").toExternalForm());

        stage.setMaximized(true);
        stage.setTitle("Banking.fx");
        stage.getIcons().add(ImageProvider.readImageFromMetaInf(ImageType.MAIN));
        stage.setScene(scene);

        stage.show();
    }

}
