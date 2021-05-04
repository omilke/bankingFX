package de.omilke.bankingfx.report.savings.model

import javafx.beans.property.*
import java.math.BigDecimal
import java.time.LocalDate
import de.omilke.banking.account.entity.Entry as domainEntry

/**
 * @author Oliver Milke
 */
class Entry {

    val category: String?

    private val entryDate: ObjectProperty<LocalDate?>
    private val amount: ObjectProperty<BigDecimal?>
    private val saving: ObjectProperty<Boolean?>
    private val comment: StringProperty

    private val groupLabel: StringProperty

    constructor(entry: domainEntry)
            : this(entry.entryDate, entry.amount, entry.isSaving, entry.category, entry.comment)

    constructor(groupLabel: String?, sum: BigDecimal?)
            : this(null, sum, null, null, null, groupLabel)

    constructor(entryDate: LocalDate? = null, amount: BigDecimal? = null, saving: Boolean? = false, category: String? = null, comment: String? = null, groupLabel: String? = null) {

        this.category = category

        this.entryDate = SimpleObjectProperty(entryDate)
        this.amount = SimpleObjectProperty(amount)
        this.saving = SimpleObjectProperty(saving)
        this.comment = SimpleStringProperty(comment)
        this.groupLabel = SimpleStringProperty(groupLabel)
    }

    fun getEntryDate(): LocalDate? {
        return entryDate.get()
    }

    fun entryDateProperty(): ObjectProperty<LocalDate?> {
        return entryDate
    }

    fun getAmount(): BigDecimal? {
        return amount.get()
    }

    fun amountProperty(): ObjectProperty<BigDecimal?> {
        return amount
    }

    fun savingProperty(): ObjectProperty<Boolean?> {
        return saving
    }

    fun getComment(): String {
        return comment.get()
    }

    fun commentProperty(): StringProperty {
        return comment
    }

    fun groupLabelProperty(): StringProperty {
        return groupLabel
    }

}

