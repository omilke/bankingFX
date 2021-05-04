package de.omilke.bankingfx.report.savings.model;

import javafx.beans.property.*;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * @author Oliver Milke
 */
public class Entry {

    private final ObjectProperty<LocalDate> entryDate;
    private final ObjectProperty<BigDecimal> amount;
    private final BooleanProperty saving;
    private final StringProperty comment;

    private final String category;

    private final StringProperty groupLabel;

    public Entry(final String groupLabel, BigDecimal sum) {

        this.entryDate = new SimpleObjectProperty<>(null);
        this.saving = new SimpleBooleanProperty();
        this.comment = new SimpleStringProperty(null);

        this.category = null;

        this.groupLabel = new SimpleStringProperty(groupLabel);
        this.amount = new SimpleObjectProperty<>(sum);
    }

    public Entry() {

        this(null, null, false, null, null);
    }

    public Entry(final LocalDate entryDate, final BigDecimal amount, final boolean saving, final String category, final String comment) {

        this.entryDate = new SimpleObjectProperty<>(entryDate);
        this.amount = new SimpleObjectProperty<>(amount);
        this.saving = new SimpleBooleanProperty(saving);
        this.comment = new SimpleStringProperty(comment);

        this.category = category;

        this.groupLabel = new SimpleStringProperty(null);
    }

    public LocalDate getEntryDate() {

        return entryDate.get();
    }

    public ObjectProperty<LocalDate> entryDateProperty() {

        return entryDate;
    }

    public BigDecimal getAmount() {

        return amount.get();
    }

    public ObjectProperty<BigDecimal> amountProperty() {

        return amount;
    }

    public BooleanProperty savingProperty() {

        return saving;
    }

    public String getComment() {

        return comment.get();
    }

    public StringProperty commentProperty() {

        return comment;
    }

    public String getCategory() {

        return category;
    }

    public StringProperty groupLabelProperty() {

        return groupLabel;
    }

    public static Entry fromStorageModel(de.omilke.banking.account.entity.Entry current) {

        return new Entry(current.getEntryDate(), current.getAmount(), current.isSaving(), current.getCategory(), current.getComment());
    }
}
