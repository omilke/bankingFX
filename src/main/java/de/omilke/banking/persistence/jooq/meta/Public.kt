/*
 * This file is generated by jOOQ.
 */
package de.omilke.banking.persistence.jooq.meta


import de.omilke.banking.persistence.jooq.meta.tables.Entry
import de.omilke.banking.persistence.jooq.meta.tables.RecurringEntry

import kotlin.collections.List

import org.jooq.Catalog
import org.jooq.Table
import org.jooq.impl.SchemaImpl


/**
 * This class is generated by jOOQ.
 */
@Suppress("UNCHECKED_CAST")
open class Public : SchemaImpl("PUBLIC", DefaultCatalog.DEFAULT_CATALOG) {
    companion object {

        /**
         * The reference instance of <code>PUBLIC</code>
         */
        val PUBLIC = Public()
    }

    /**
     * The table <code>PUBLIC.ENTRY</code>.
     */
    val ENTRY get() = Entry.ENTRY

    /**
     * The table <code>PUBLIC.RECURRING_ENTRY</code>.
     */
    val RECURRING_ENTRY get() = RecurringEntry.RECURRING_ENTRY

    override fun getCatalog(): Catalog = DefaultCatalog.DEFAULT_CATALOG

    override fun getTables(): List<Table<*>> = listOf(
        Entry.ENTRY,
        RecurringEntry.RECURRING_ENTRY
    )
}
