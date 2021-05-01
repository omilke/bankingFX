package de.omilke.bankingfx.controls;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.omilke.bankingfx.main.entrylist.model.Entry;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableCell;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.math.BigDecimal;

public class SavingEditingCell<P> extends TableCell<P, Boolean> {

    private CheckBox checkBox;

    public SavingEditingCell() {

        setText(null);
        createComboBox();
    }

    @Override
    public void startEdit() {

        if (!isEmpty()) {
            super.startEdit();

            this.displayEditControl(getItem());

            this.checkBox.requestFocus();
        }
    }

    @Override
    public void cancelEdit() {

        super.cancelEdit();

        displayValue(getItem());
    }

    @Override
    public void updateItem(final Boolean item, final boolean empty) {

        super.updateItem(item, empty);

        if (empty) {
            setGraphic(null);
        } else {
            if (isEditing()) {

                displayEditControl(item);
            } else {

                displayValue(item);
            }
        }
    }

    private void displayValue(final Boolean item) {

        final Text icon;
        if (item) {

            // this is a saving -> define the direction of the saving

            final Entry rowItem = (Entry) getTableRow().getItem();
            final BigDecimal amount = rowItem.getAmount();

            if (amount.compareTo(BigDecimal.ZERO) < 0) {
                icon = UIUtils.INSTANCE.getIconWithColor(FontAwesomeIcon.DOWNLOAD, Color.GREEN);

            } else {
                icon = UIUtils.INSTANCE.getIconWithColor(FontAwesomeIcon.UPLOAD, Color.RED);
            }
        } else {
            // not a saving -> no graphic
            icon = null;
        }

        setGraphic(icon);
    }

    private void displayEditControl(final Boolean item) {

        if (checkBox != null) {
            checkBox.setSelected(item);
        }

        setGraphic(checkBox);
    }

    private void createComboBox() {

        checkBox = new CheckBox();
        checkBox.setOnAction((e) -> commitEdit(checkBox.selectedProperty().getValue()));
    }

}