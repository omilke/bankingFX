package de.omilke.bankingfx.controls;

import javafx.scene.control.ResizeFeaturesBase;
import javafx.util.Callback;

/**
 * Makes a column always resizable, which does not seem to be configurable in FXML.
 */
public class ResizeableColumnCallback<T extends ResizeFeaturesBase> implements Callback<T, Boolean> {

    @Override
    public Boolean call(T param) {

        return true;
    }
}
