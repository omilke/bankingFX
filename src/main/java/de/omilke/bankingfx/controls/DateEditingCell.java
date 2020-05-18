package de.omilke.bankingfx.controls;

import javafx.scene.control.DatePicker;
import javafx.scene.control.TableCell;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateEditingCell<P> extends TableCell<P, LocalDate> {

    private DatePicker datePicker;

    private final DateTimeFormatter formatter;

    public DateEditingCell(final DateTimeFormatter formatter) {

        this.formatter = formatter;
    }

    @Override
    public void startEdit() {

        if (!isEmpty()) {
            super.startEdit();
            createDatePicker();

            displayEditControl(getItem());

            this.datePicker.show();
        }
    }

    @Override
    public void cancelEdit() {

        super.cancelEdit();

        displayValue(getItem());
    }

    @Override
    public void updateItem(final LocalDate item, final boolean empty) {

        super.updateItem(item, empty);

        if (empty) {
            setText(null);
            setGraphic(null);
        } else {
            if (isEditing()) {

                displayEditControl(item);
            } else {

                displayValue(item);
            }
        }
    }

    private void displayValue(final LocalDate item) {

        setText(item.format(formatter));
        setGraphic(null);
    }

    private void displayEditControl(final LocalDate item) {

        if (datePicker != null) {
            datePicker.setValue(item);
        }

        setText(null);
        setGraphic(datePicker);
    }

    private void createDatePicker() {

        datePicker = new DatePicker(getItem());
        datePicker.setOnAction((e) -> commitEdit(this.datePicker.getValue()));
    }

}