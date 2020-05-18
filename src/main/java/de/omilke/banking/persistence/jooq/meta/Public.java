/*
 * This file is generated by jOOQ.
 */
package de.omilke.banking.persistence.jooq.meta;


import de.omilke.banking.persistence.jooq.meta.tables.Entry;
import de.omilke.banking.persistence.jooq.meta.tables.RecurringEntry;
import org.jooq.Catalog;
import org.jooq.Table;
import org.jooq.impl.SchemaImpl;

import java.util.Arrays;
import java.util.List;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({"all", "unchecked", "rawtypes"})
public class Public extends SchemaImpl {

    private static final long serialVersionUID = -1279183224;

    /**
     * The reference instance of <code>PUBLIC</code>
     */
    public static final Public PUBLIC = new Public();

    /**
     * The table <code>PUBLIC.ENTRY</code>.
     */
    public final Entry ENTRY = Entry.ENTRY;

    /**
     * The table <code>PUBLIC.RECURRING_ENTRY</code>.
     */
    public final RecurringEntry RECURRING_ENTRY = RecurringEntry.RECURRING_ENTRY;

    /**
     * No further instances allowed
     */
    private Public() {
        super("PUBLIC", null);
    }


    @Override
    public Catalog getCatalog() {
        return DefaultCatalog.DEFAULT_CATALOG;
    }

    @Override
    public final List<Table<?>> getTables() {
        return Arrays.<Table<?>>asList(
                Entry.ENTRY,
                RecurringEntry.RECURRING_ENTRY);
    }
}
