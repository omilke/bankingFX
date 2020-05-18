package de.omilke.bankingfx.controls;

import javafx.scene.control.TableCell;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

import java.math.BigDecimal;
import java.util.Locale;

public class AmountEditingCell<P> extends TableCell<P, BigDecimal> {

    private final BigDecimalStringConverter converter;

    private TextField text;

    public AmountEditingCell(final Locale locale) {

        createComboBox();

        this.converter = new BigDecimalStringConverter(locale);
    }

    @Override
    public void startEdit() {

        if (!isEmpty()) {
            super.startEdit();

            this.displayEditControl(getItem());

            this.text.requestFocus();
        }
    }

    @Override
    public void cancelEdit() {

        super.cancelEdit();

        displayValue(getItem());
    }

    @Override
    public void updateItem(final BigDecimal item, final boolean empty) {

        super.updateItem(item, empty);

        Object o = getTableRow().getItem();

        if (empty || o == null) {
            setGraphic(null);
            setText(null);
        } else {
            if (isEditing()) {

                displayEditControl(item);
            } else {

                displayValue(item);
            }
        }
    }

    private void displayValue(final BigDecimal item) {

        setText(converter.toString(item));

        if (item.compareTo(BigDecimal.ZERO) >= 0) {
            setTextFill(Color.GREEN);
        } else {
            setTextFill(Color.RED);
        }

        setGraphic(null);

    }

    private void displayEditControl(final BigDecimal item) {

        if (text != null) {
            text.setText(converter.toString(item));
        }

        setText(null);
        setGraphic(text);
    }

    private void createComboBox() {

        text = new TextField();
        text.setOnAction((e) -> {
            final String value = text.textProperty().getValue();

            final BigDecimal fromString = converter.fromString(value);

            if (fromString != null) {
                commitEdit(fromString);
            } else {
                cancelEdit();
            }

        });
    }

}