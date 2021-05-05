package de.omilke.banking.persistence.jooq

import de.omilke.banking.BankingConfigurator
import de.omilke.banking.ConfigurationConstants
import de.omilke.banking.account.entity.Entry
import de.omilke.banking.account.entity.RecurringEntry
import de.omilke.banking.persistence.jooq.mapper.EntryMapper
import de.omilke.banking.persistence.jooq.mapper.RecurringEntryMapper
import de.omilke.banking.persistence.jooq.mapper.StrategyMapper
import de.omilke.banking.persistence.jooq.meta.tables.Entry.Companion.ENTRY
import de.omilke.banking.persistence.jooq.meta.tables.RecurringEntry.Companion.RECURRING_ENTRY
import org.apache.logging.log4j.Level
import org.apache.logging.log4j.LogManager
import org.jooq.Condition
import org.jooq.Record5
import org.jooq.Result
import org.jooq.impl.DSL.*
import java.math.BigDecimal
import java.time.LocalDate


class JooqQueryExecutor(private val jooqContext: JooqContext) {

    constructor() : this(JooqContext.DEFAULT_CONTEXT)

    internal fun persist(entry: Entry) {

        jooqContext.context
                .insertInto(
                        ENTRY,
                        ENTRY.AMOUNT,
                        ENTRY.ENTRYDATE,
                        ENTRY.COMMENT,
                        ENTRY.CATEGORY,
                        ENTRY.SAVING,
                        ENTRY.SEQUENCE,
                        ENTRY.ORDERINDEX,
                        ENTRY.YEAR,
                        ENTRY.MONTH,
                        ENTRY.DAY)
                .values(
                        entry.amount,
                        entry.entryDate,
                        entry.comment,
                        entry.category,
                        entry.isSaving,
                        entry.sequence.ordinal,
                        entry.orderIndex,
                        entry.entryDate.year,
                        entry.entryDate.monthValue,
                        entry.entryDate.dayOfMonth)
                .execute()

    }

    internal fun persist(recurringEntry: RecurringEntry) {

        jooqContext.context
                .insertInto(
                        RECURRING_ENTRY,
                        RECURRING_ENTRY.AMOUNT,
                        RECURRING_ENTRY.STARTOFRECURRENCE,
                        RECURRING_ENTRY.LASTRECURRENCE,
                        RECURRING_ENTRY.COMMENT,
                        RECURRING_ENTRY.CATEGORY,
                        RECURRING_ENTRY.SAVING,
                        RECURRING_ENTRY.SEQUENCE,
                        RECURRING_ENTRY.ORDERINDEX,
                        RECURRING_ENTRY.RECURRENCESTRATEGY)
                .values(
                        recurringEntry.amount,
                        recurringEntry.startOfRecurrence,
                        recurringEntry.lastRecurrence,
                        recurringEntry.comment,
                        recurringEntry.category,
                        recurringEntry.isSaving,
                        recurringEntry.sequence.ordinal,
                        recurringEntry.orderIndex,
                        StrategyMapper.getLabelForStrategy(recurringEntry.recurrenceStrategy))
                .execute()
    }


    fun selectEntries(lowerBound: LocalDate?, upperBound: LocalDate?, category: String?): MutableList<Entry> {

        return jooqContext.context
                .selectFrom(ENTRY)
                .where(buildWhereCondition(lowerBound, upperBound, category))
                .orderBy(
                        ENTRY.YEAR.desc(),
                        ENTRY.MONTH.desc(),
                        ENTRY.SEQUENCE,
                        ENTRY.ORDERINDEX,
                        ENTRY.DAY,
                        ENTRY.AMOUNT)

                .fetch()
                .map(EntryMapper)
    }

    fun selectRecurringEntries(): MutableList<RecurringEntry> {

        return jooqContext.context
                .selectFrom(RECURRING_ENTRY)
                .orderBy(
                        RECURRING_ENTRY.RECURRENCESTRATEGY.desc(),
                        RECURRING_ENTRY.SEQUENCE,
                        RECURRING_ENTRY.ORDERINDEX,
                        RECURRING_ENTRY.AMOUNT,
                        RECURRING_ENTRY.CATEGORY,
                        RECURRING_ENTRY.COMMENT,
                        RECURRING_ENTRY.LASTRECURRENCE.asc().nullsFirst()
                )
                .fetch()
                .map(RecurringEntryMapper)
    }

    fun getSumsByMonthAndCategory(start: LocalDate?, end: LocalDate?): Result<Record5<String?, Int?, Int?, BigDecimal, String>> {

        val whereCondition = buildWhereCondition(start, end, null)

        return jooqContext.context
                //sum by period and category
                .select(ENTRY.CATEGORY, ENTRY.YEAR, ENTRY.MONTH, sum(ENTRY.AMOUNT), inline("A").`as`("RECORDTYPE"))
                .from(ENTRY)
                .where(whereCondition)
                .groupBy(ENTRY.YEAR, ENTRY.MONTH, ENTRY.CATEGORY)
                .union(
                        //sum by category over time
                        select(ENTRY.CATEGORY, inline(null as Int?), inline(null as Int?), sum(ENTRY.AMOUNT), inline("B").`as`("RECORDTYPE"))
                                .from(ENTRY)
                                .where(whereCondition)
                                .groupBy(ENTRY.CATEGORY))
                .union(
                        //sum by period over all categories
                        select(inline(null as String?), ENTRY.YEAR, ENTRY.MONTH, sum(ENTRY.AMOUNT), inline("C").`as`("RECORDTYPE"))
                                .from(ENTRY)
                                .where(whereCondition)
                                .groupBy(ENTRY.YEAR, ENTRY.MONTH))
                .union(
                        //sum over all periods and categories
                        select(inline(null as String?), inline(null as Int?), inline(null as Int?), sum(ENTRY.AMOUNT), inline("D").`as`("RECORDTYPE"))
                                .from(ENTRY)
                                .where(whereCondition))
                .orderBy(field(ENTRY.YEAR).asc().nullsLast(), field(ENTRY.MONTH).asc().nullsLast(), field("RECORDTYPE"), field(ENTRY.CATEGORY).asc().nullsLast())
                .fetch()
    }

    private fun buildWhereCondition(start: LocalDate?, end: LocalDate?, category: String?): List<Condition> {

        val conditions = ArrayList<Condition>()

        if (start != null)
            conditions.add(ENTRY.ENTRYDATE.ge(start))

        if (end != null)
            conditions.add(ENTRY.ENTRYDATE.le(end))

        if (category != null)
            conditions.add(ENTRY.CATEGORY.eq(category))

        return conditions
    }

    internal fun commit() {

        jooqContext.commit()
    }

    internal fun checkPersistenceLayerReadiness() {

        if (BankingConfigurator.isPropertyEnabled(ConfigurationConstants.IMPORT_ACTIVE)) {
            LOGGER.log(Level.INFO, "skipping persistence readiness check due to impending import")
        } else {
            try {
                LOGGER.log(Level.INFO, "starting persistence readiness check for JOOQ + h2")

                val count = jooqContext.context
                        .selectCount()
                        .from(ENTRY)
                        .fetch()[0]
                        .value1()

                LOGGER.log(Level.INFO, "completed persistence readiness check JOOQ + h2, found {} entries", count)
            } catch (e: Exception) {

                LOGGER.log(Level.FATAL, "Persistence Layer cannot be initialized due to underlying exception:", e)
                throw  Exception("banking.fx cannot be initialized due to underlying exception")
            }
        }
    }

    fun purgeAndPrepareDatabase() {

        val resource = JooqQueryExecutor::class.java.classLoader.getResource("META-INF/sql/init.sql")
        LOGGER.log(Level.INFO, "Dropping and re-creating tables for JOOQ from source '{}'", resource)

        jooqContext.context.execute(resource!!.readText(Charsets.UTF_8))
    }

    companion object {
        private val LOGGER = LogManager.getLogger(JooqQueryExecutor::class.java)
    }


}
