//package de.omilke.banking.persistence.jpa
//
//import de.omilke.banking.account.entity.Entry
//import de.omilke.banking.account.entity.EntryRepository
//import org.apache.logging.log4j.LogManager
//import de.omilke.banking.storage.Jpa
//
//import java.time.LocalDate
//import java.time.YearMonth
//
//class EntryRepositoryJpa : EntryRepository {
//
//    private val em = Jpa.entityManager
//
//    override fun update(entry: Entry): Entry {
//
//        val transaction = this.em.transaction
//        transaction.begin()
//
//        val result = this.em.merge(entry)
//
//        transaction.commit()
//
//        return result
//    }
//
//    override fun findAllEntries(): List<Entry> {
//
//        LOGGER.info("Selecting entries from DB: all ")
//
//        val query = this.em.createNamedQuery(Entry.SELECT_ALL, Entry::class.java)
//        return query.resultList
//    }
//
//    override fun findAllEntriesFrom(start: LocalDate): List<Entry> {
//
//        LOGGER.info("Selecting entries from DB: > $start ")
//
//        val query = this.em.createNamedQuery(Entry.SELECT_ALL_AFTER, Entry::class.java)
//        query.setParameter("after", start.minusDays(1))
//
//        return query.resultList
//    }
//
//    override fun findAllEntriesUpTo(to: LocalDate): List<Entry> {
//
//        LOGGER.info("Selecting entries from DB: < $to ")
//
//        val query = this.em.createNamedQuery(Entry.SELECT_ALL_BEFORE, Entry::class.java)
//        query.setParameter("before", to.plusDays(1))
//
//        return query.resultList
//    }
//
//    override fun findAllEntriesBetween(start: YearMonth, end: YearMonth): List<Entry> {
//
//        return this.findAllEntriesBetween(start.atDay(1), end.atEndOfMonth())
//    }
//
//    override fun findAllEntriesBetween(start: LocalDate, end: LocalDate?): List<Entry> {
//
//        return if (end == null || LocalDate.MAX == end) {
//
//            this.findAllEntriesFrom(start)
//        } else {
//
//            LOGGER.info("Selecting entries from DB: $start - $end")
//
//            val query = this.em.createNamedQuery(Entry.SELECT_ALL_BETWEEN, Entry::class.java)
//            query.setParameter("after", start.minusDays(1))
//            query.setParameter("before", end.plusDays(1))
//
//            query.resultList
//        }
//    }
//
//    override fun findAllEntriesBetweenWithCategoryName(start: LocalDate, end: LocalDate?, name: String): List<Entry> {
//
//        val filteredByCategory = findAllEntriesBetween(start, end).filter { it.category.name == name }
//
//        LOGGER.info("...applied filtering categories by name: $name")
//
//        return filteredByCategory
//    }
//
//    companion object {
//
//        private val LOGGER = LogManager.getLogger(Entry::class.java)
//    }
//}
