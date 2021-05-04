package de.omilke.bankingfx.report.categories.control;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.omilke.banking.account.entity.Entry;
import de.omilke.bankingfx.UIConstants;
import de.omilke.bankingfx.controls.UIUtils;
import javafx.scene.control.TableCell;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.math.BigDecimal;

/**
 * Created by Olli on 27.05.2017.
 */
public class SavingTableCell extends TableCell<Entry, Boolean> {

    @Override
    protected void updateItem(final Boolean item, final boolean empty) {

        super.updateItem(item, empty);

        Object rowItem = getTableRow().getItem();
        if (!empty && item != null && item && rowItem != null) {

            final Entry selectedEntry = (Entry) rowItem;
            getStyleClass().add(UIConstants.ALIGN_CENTER);

            final BigDecimal amount = selectedEntry.getAmount();

            final Text icon;

            if (amount.compareTo(BigDecimal.ZERO) < 0) {
                icon = UIUtils.INSTANCE.getIconWithColor(FontAwesomeIcon.DOWNLOAD, Color.GREEN);
            } else {
                icon = UIUtils.INSTANCE.getIconWithColor(FontAwesomeIcon.UPLOAD, Color.RED);
            }

            setGraphic(icon);

        } else {
            setGraphic(null);
        }

    }
}
