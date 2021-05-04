package de.omilke.bankingfx.report.savings.model

import de.omilke.bankingfx.controls.UIUtils
import javafx.beans.property.*
import java.math.BigDecimal
import java.time.LocalDate
import de.omilke.banking.account.entity.Entry as domainEntry

//TODO: unify with EntryTableRow, which in the end is the same thing (maybe differently constructed)
class Entry {

    private val description: StringProperty
    private val entryDate: ObjectProperty<LocalDate?>
    private val amount: ObjectProperty<BigDecimal?>
    private val saving: ObjectProperty<Boolean?>
    private val comment: StringProperty

    val isGroupElement: Boolean

    constructor()
            : this("", true)

    constructor(entry: domainEntry)
            : this(UIUtils.formatEntryDate(entry.entryDate), false, entry.amount, entry.entryDate, entry.isSaving, entry.comment)

    constructor(groupLabel: String, sum: BigDecimal?)
            : this(groupLabel, true, sum)

    constructor(description: String,
                groupElement: Boolean,
                amount: BigDecimal? = null,
                entryDate: LocalDate? = null,
                saving: Boolean? = null,
                comment: String? = null) {

        this.description = SimpleStringProperty(description)

        this.entryDate = SimpleObjectProperty(entryDate)
        this.amount = SimpleObjectProperty(amount)
        this.saving = SimpleObjectProperty(saving)
        this.comment = SimpleStringProperty(comment)

        this.isGroupElement = groupElement
    }

    fun descriptionProperty(): StringProperty {
        return description
    }

    fun entryDateProperty(): ObjectProperty<LocalDate?> {
        return entryDate
    }

    fun getEntryDate() = entryDateProperty().get()

    fun amountProperty(): ObjectProperty<BigDecimal?> {
        return amount
    }

    fun getAmount() = amount.get()

    fun savingProperty(): ObjectProperty<Boolean?> {
        return saving
    }

    fun commentProperty(): StringProperty {
        return comment
    }

    fun getComment() = comment.get()

}

