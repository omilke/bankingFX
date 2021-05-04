package de.omilke.bankingfx.main.entrylist.model;

import de.omilke.banking.account.entity.EntrySequence;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * @author Oliver Milke
 */
public class Entry {

    private final ObjectProperty<LocalDate> entryDate;
    private final SimpleObjectProperty<EntryOrder> entryOrder;
    private final ObjectProperty<BigDecimal> amount;
    private final BooleanProperty saving;
    private final StringProperty category;
    private final StringProperty comment;

    private final boolean groupElement;

    public Entry(final LocalDate month) {

        this.entryDate = new SimpleObjectProperty<>(month);
        this.entryOrder = new SimpleObjectProperty<>(null);
        this.amount = new SimpleObjectProperty<>(null);
        this.saving = new SimpleBooleanProperty();
        this.category = new SimpleStringProperty(null);
        this.comment = new SimpleStringProperty(null);

        this.groupElement = true;
    }

    public Entry(final de.omilke.banking.account.entity.Entry entry) {

        this(
                entry.getEntryDate(),
                entry.getSequence(),
                entry.getOrderIndex(),
                entry.getAmount(),
                entry.isSaving(),
                entry.getCategory(),
                entry.getComment());
    }

    public Entry(final LocalDate entryDate, final EntrySequence sequence, final int orderIndex, final BigDecimal amount, final boolean saving,
                 final String category, final String comment) {

        this.entryDate = new SimpleObjectProperty<>(entryDate);
        this.entryOrder = new SimpleObjectProperty<>(EntryOrder.of(entryDate, sequence, orderIndex));
        this.amount = new SimpleObjectProperty<>(amount);
        this.saving = new SimpleBooleanProperty(saving);
        this.category = new SimpleStringProperty(category);
        this.comment = new SimpleStringProperty(comment);

        this.groupElement = false;

        this.entryDate.addListener(new DateChangeListener());
    }

    public LocalDate getEntryDate() {

        return entryDate.get();
    }

    public void setEntryDate(final LocalDate date) {

        entryDate.set(date);
    }

    public ObjectProperty<LocalDate> entryDateProperty() {

        return entryDate;
    }

    public EntryOrder getEntryOrder() {

        return entryOrder.get();
    }

    public void setEntryOrder(final EntryOrder entryOrder) {

        this.entryOrder.set(entryOrder);
    }

    public SimpleObjectProperty<EntryOrder> entryOrderProperty() {

        return entryOrder;
    }

    public BigDecimal getAmount() {

        return amount.get();
    }

    public void setAmount(final BigDecimal amount) {

        this.amount.set(amount);
    }

    public ObjectProperty<BigDecimal> amountProperty() {

        return amount;
    }

    public Boolean getSaving() {

        return saving.get();
    }

    public void setSaving(final boolean saving) {

        this.saving.set(saving);
    }

    public BooleanProperty savingProperty() {

        return saving;
    }

    public String getCategory() {

        return category.get();
    }

    public void setCategory(final String category) {

        this.category.set(category);
    }

    public StringProperty categoryProperty() {

        return category;
    }

    public String getComment() {

        return comment.get();
    }

    public void setComment(final String comment) {

        this.comment.set(comment);
    }

    public StringProperty commentProperty() {

        return comment;
    }

    public boolean isGroupElement() {

        return groupElement;
    }

    private final class DateChangeListener implements ChangeListener<LocalDate> {

        @Override
        public void changed(final ObservableValue<? extends LocalDate> observable, final LocalDate oldValue, final LocalDate newValue) {

            // reflect the date change into the depending property
            entryOrder.set(getEntryOrder().update(newValue));
        }
    }
}
