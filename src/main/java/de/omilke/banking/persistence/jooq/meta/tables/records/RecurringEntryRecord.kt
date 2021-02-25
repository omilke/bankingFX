/*
 * This file is generated by jOOQ.
 */
package de.omilke.banking.persistence.jooq.meta.tables.records


import de.omilke.banking.persistence.jooq.meta.tables.RecurringEntry

import java.math.BigDecimal
import java.time.LocalDate

import org.jooq.Field
import org.jooq.Record1
import org.jooq.Record10
import org.jooq.Row10
import org.jooq.impl.UpdatableRecordImpl


/**
 * This class is generated by jOOQ.
 */
@Suppress("UNCHECKED_CAST")
open class RecurringEntryRecord() : UpdatableRecordImpl<RecurringEntryRecord>(RecurringEntry.RECURRING_ENTRY), Record10<Long?, BigDecimal?, LocalDate?, LocalDate?, String?, String?, Boolean?, Int?, Int?, String?> {

    var id: Long?
        set(value) = set(0, value)
        get() = get(0) as Long?

    var amount: BigDecimal?
        set(value) = set(1, value)
        get() = get(1) as BigDecimal?

    var startofrecurrence: LocalDate?
        set(value) = set(2, value)
        get() = get(2) as LocalDate?

    var lastrecurrence: LocalDate?
        set(value) = set(3, value)
        get() = get(3) as LocalDate?

    var comment: String?
        set(value) = set(4, value)
        get() = get(4) as String?

    var category: String?
        set(value) = set(5, value)
        get() = get(5) as String?

    var saving: Boolean?
        set(value) = set(6, value)
        get() = get(6) as Boolean?

    var sequence: Int?
        set(value) = set(7, value)
        get() = get(7) as Int?

    var orderindex: Int?
        set(value) = set(8, value)
        get() = get(8) as Int?

    var recurrencestrategy: String?
        set(value) = set(9, value)
        get() = get(9) as String?

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    override fun key(): Record1<Long?> = super.key() as Record1<Long?>

    // -------------------------------------------------------------------------
    // Record10 type implementation
    // -------------------------------------------------------------------------

    override fun fieldsRow(): Row10<Long?, BigDecimal?, LocalDate?, LocalDate?, String?, String?, Boolean?, Int?, Int?, String?> = super.fieldsRow() as Row10<Long?, BigDecimal?, LocalDate?, LocalDate?, String?, String?, Boolean?, Int?, Int?, String?>
    override fun valuesRow(): Row10<Long?, BigDecimal?, LocalDate?, LocalDate?, String?, String?, Boolean?, Int?, Int?, String?> = super.valuesRow() as Row10<Long?, BigDecimal?, LocalDate?, LocalDate?, String?, String?, Boolean?, Int?, Int?, String?>
    override fun field1(): Field<Long?> = RecurringEntry.RECURRING_ENTRY.ID
    override fun field2(): Field<BigDecimal?> = RecurringEntry.RECURRING_ENTRY.AMOUNT
    override fun field3(): Field<LocalDate?> = RecurringEntry.RECURRING_ENTRY.STARTOFRECURRENCE
    override fun field4(): Field<LocalDate?> = RecurringEntry.RECURRING_ENTRY.LASTRECURRENCE
    override fun field5(): Field<String?> = RecurringEntry.RECURRING_ENTRY.COMMENT
    override fun field6(): Field<String?> = RecurringEntry.RECURRING_ENTRY.CATEGORY
    override fun field7(): Field<Boolean?> = RecurringEntry.RECURRING_ENTRY.SAVING
    override fun field8(): Field<Int?> = RecurringEntry.RECURRING_ENTRY.SEQUENCE
    override fun field9(): Field<Int?> = RecurringEntry.RECURRING_ENTRY.ORDERINDEX
    override fun field10(): Field<String?> = RecurringEntry.RECURRING_ENTRY.RECURRENCESTRATEGY
    override fun component1(): Long? = id
    override fun component2(): BigDecimal? = amount
    override fun component3(): LocalDate? = startofrecurrence
    override fun component4(): LocalDate? = lastrecurrence
    override fun component5(): String? = comment
    override fun component6(): String? = category
    override fun component7(): Boolean? = saving
    override fun component8(): Int? = sequence
    override fun component9(): Int? = orderindex
    override fun component10(): String? = recurrencestrategy
    override fun value1(): Long? = id
    override fun value2(): BigDecimal? = amount
    override fun value3(): LocalDate? = startofrecurrence
    override fun value4(): LocalDate? = lastrecurrence
    override fun value5(): String? = comment
    override fun value6(): String? = category
    override fun value7(): Boolean? = saving
    override fun value8(): Int? = sequence
    override fun value9(): Int? = orderindex
    override fun value10(): String? = recurrencestrategy

    override fun value1(value: Long?): RecurringEntryRecord {
        this.id = value
        return this
    }

    override fun value2(value: BigDecimal?): RecurringEntryRecord {
        this.amount = value
        return this
    }

    override fun value3(value: LocalDate?): RecurringEntryRecord {
        this.startofrecurrence = value
        return this
    }

    override fun value4(value: LocalDate?): RecurringEntryRecord {
        this.lastrecurrence = value
        return this
    }

    override fun value5(value: String?): RecurringEntryRecord {
        this.comment = value
        return this
    }

    override fun value6(value: String?): RecurringEntryRecord {
        this.category = value
        return this
    }

    override fun value7(value: Boolean?): RecurringEntryRecord {
        this.saving = value
        return this
    }

    override fun value8(value: Int?): RecurringEntryRecord {
        this.sequence = value
        return this
    }

    override fun value9(value: Int?): RecurringEntryRecord {
        this.orderindex = value
        return this
    }

    override fun value10(value: String?): RecurringEntryRecord {
        this.recurrencestrategy = value
        return this
    }

    override fun values(value1: Long?, value2: BigDecimal?, value3: LocalDate?, value4: LocalDate?, value5: String?, value6: String?, value7: Boolean?, value8: Int?, value9: Int?, value10: String?): RecurringEntryRecord {
        this.value1(value1)
        this.value2(value2)
        this.value3(value3)
        this.value4(value4)
        this.value5(value5)
        this.value6(value6)
        this.value7(value7)
        this.value8(value8)
        this.value9(value9)
        this.value10(value10)
        return this
    }

    /**
     * Create a detached, initialised RecurringEntryRecord
     */
    constructor(id: Long? = null, amount: BigDecimal? = null, startofrecurrence: LocalDate? = null, lastrecurrence: LocalDate? = null, comment: String? = null, category: String? = null, saving: Boolean? = null, sequence: Int? = null, orderindex: Int? = null, recurrencestrategy: String? = null): this() {
        this.id = id
        this.amount = amount
        this.startofrecurrence = startofrecurrence
        this.lastrecurrence = lastrecurrence
        this.comment = comment
        this.category = category
        this.saving = saving
        this.sequence = sequence
        this.orderindex = orderindex
        this.recurrencestrategy = recurrencestrategy
    }
}
