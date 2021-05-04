package de.omilke.bankingfx.main.entrylist.model

import de.omilke.banking.account.entity.Entry
import javafx.beans.property.ObjectProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import javafx.beans.property.StringProperty
import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue
import java.math.BigDecimal
import java.time.LocalDate

/**
 * A model class for use in the main TreeTableView to display grouped entries.
 *
 */
class EntryTableRow {

    private val entryDate: ObjectProperty<LocalDate>
    private val entryOrder: SimpleObjectProperty<EntryOrder?>
    private val amount: ObjectProperty<BigDecimal?>
    private val saving: ObjectProperty<Boolean?>
    private val category: StringProperty
    private val comment: StringProperty

    val isGroupElement: Boolean

    /**
     * Provides a table row with only a month as grouping element
     */
    constructor(month: LocalDate) :
            this(month, null, null, null, null, null, true)

    /**
     * Provides a table row in order to display an entry.
     */
    constructor(entry: Entry) :
            this(entry.entryDate, entry.buildEntryOrder(), entry.amount, entry.isSaving, entry.category, entry.comment, false)

    /**
     * Provides a table row for use as root row.
     */
    constructor() :
            this(LocalDate.MAX, null, null, null, null, null, true)

    constructor(entryDate: LocalDate, entryOrder: EntryOrder?, amount: BigDecimal?, saving: Boolean?, category: String?, comment: String?,
                groupElement: Boolean) {

        this.entryDate = SimpleObjectProperty(entryDate)
        this.entryDate.addListener(DateChangeListener())
        this.entryOrder = SimpleObjectProperty(entryOrder)
        this.amount = SimpleObjectProperty(amount)
        this.saving = SimpleObjectProperty(saving)
        this.category = SimpleStringProperty(category)
        this.comment = SimpleStringProperty(comment)

        isGroupElement = groupElement
    }

    fun getEntryDate(): LocalDate {
        return entryDate.get()
    }

    fun setEntryDate(date: LocalDate) {
        entryDate.set(date)
    }

    fun entryDateProperty(): ObjectProperty<LocalDate> {
        return entryDate
    }

    fun getEntryOrder(): EntryOrder? {
        return entryOrder.get()
    }

    fun setEntryOrder(entryOrder: EntryOrder?) {
        this.entryOrder.set(entryOrder)
    }

    fun entryOrderProperty(): SimpleObjectProperty<EntryOrder?> {
        return entryOrder
    }

    fun getAmount(): BigDecimal? {
        return amount.get()
    }

    fun setAmount(amount: BigDecimal?) {
        this.amount.set(amount)
    }

    fun amountProperty(): ObjectProperty<BigDecimal?> {
        return amount
    }

    fun getSaving(): Boolean? {
        return saving.get()
    }

    fun setSaving(saving: Boolean) {
        this.saving.set(saving)
    }

    fun savingProperty(): ObjectProperty<Boolean?> {
        return saving
    }

    fun getCategory(): String {
        return category.get()
    }

    fun setCategory(category: String) {
        this.category.set(category)
    }

    fun categoryProperty(): StringProperty {
        return category
    }

    fun getComment(): String {
        return comment.get()
    }

    fun setComment(comment: String) {
        this.comment.set(comment)
    }

    fun commentProperty(): StringProperty {
        return comment
    }

    private inner class DateChangeListener : ChangeListener<LocalDate?> {

        override fun changed(observable: ObservableValue<out LocalDate?>, oldValue: LocalDate?, newValue: LocalDate?) {

            // reflect the date change into the depending property (if possible)
            val currentEntryOrder = entryOrder.get()

            if (newValue != null && currentEntryOrder != null) {
                entryOrder.set(currentEntryOrder.update(newValue))
            }

        }
    }
}