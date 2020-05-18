package de.omilke.bankingfx.controls;

import javafx.scene.control.TreeTableCell;

import java.math.BigDecimal;

/**
 * Created by Olli on 25.05.2017.
 */
public class AmountTreeTableCell<T> extends TreeTableCell<T, BigDecimal> {

    @Override
    protected void updateItem(final BigDecimal item, final boolean empty) {

        super.updateItem(item, empty);

        UIUtils.formatAmount(this, item, empty);
    }
}
