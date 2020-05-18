package de.omilke.banking.persistence.mapdb
//
//import de.omilke.banking.account.entity.EntryRepository
//import java.time.LocalDate
//import java.time.YearMonth
//import de.omilke.banking.account.entity.Entry
//import de.omilke.banking.persistence.jooq.repositories.EntryRepositoryJooq
//import org.apache.logging.log4j.Level
//
//object EntryRepositoryMapDb : EntryRepository {
//
//    override fun update(entry: Entry?): Entry {
//    }
//
//    override fun saveAll(entries: Collection<Entry>) {
//
//        val nanoTime = System.nanoTime()
//
//        EntryDatabaseAccessor.entries.clear()
//        EntryDatabaseAccessor.entries.addAll(entries)
//        EntryDatabaseAccessor.commit()
//
//        EntryRepositoryJooq.logger.log(Level.INFO, "persisting ${entries.size} entries took {}ms", (System.nanoTime() - nanoTime) / 1_000_000)
//    }
//
//
//    override fun findAllEntries(): MutableList<Entry> {
//
//        return selectSortAndFilteredBy(noFiltering())
//    }
//
//    override fun findAllEntriesFrom(start: LocalDate): MutableList<Entry> {
//
//        return selectSortAndFilteredBy { it.entryDate.isAfter(start.minusDays(1)) }
//    }
//
//    override fun findAllEntriesUpTo(to: LocalDate): MutableList<Entry> {
//
//        return selectSortAndFilteredBy { it.entryDate.isBefore(to.plusDays(1)) }
//    }
//
//    override fun findAllEntriesBetween(start: LocalDate, end: LocalDate?): MutableList<Entry> {
//
//        return if (end == null || LocalDate.MAX == end) {
//            findAllEntriesFrom(start)
//        } else {
//
//            val betweenPredicate: (Entry) -> Boolean = {
//                it.entryDate.isAfter(start.minusDays(1))
//                        && it.entryDate.isBefore(end.plusDays(1))
//            }
//
//            selectSortAndFilteredBy(betweenPredicate)
//        }
//    }
//
//    override fun findAllEntriesBetweenWithCategoryName(start: LocalDate, end: LocalDate?, name: String): MutableList<Entry> {
//
//        val upperReferenceValue =
//                if (end == null || end == LocalDate.MAX) {
//                    LocalDate.MAX
//                } else {
//                    end.plusDays(1)
//                }
//
//        val betweenAndWithCategoryPredicate: (Entry) -> Boolean = {
//            it.entryDate.isAfter(start.minusDays(1))
//                    && it.entryDate.isBefore(upperReferenceValue)
//                    && it.category.equals(name)
//        }
//
//        return selectSortAndFilteredBy(betweenAndWithCategoryPredicate)
//    }
//
//    override fun findAllEntriesBetween(start: YearMonth?, end: YearMonth?): MutableList<Entry> {
//    }
//
//    private fun selectSortAndFilteredBy(predicate: (Entry) -> Boolean): MutableList<Entry> {
//
//        val nanoTime = System.nanoTime()
//        val result = EntryDatabaseAccessor.entries.filter(predicate).sorted().toMutableList()
//
//        EntryRepositoryJooq.logger.log(Level.INFO, "fetching ${result.size} entries took {}ms", (System.nanoTime() - nanoTime) / 1_000_000)
//
//        return result
//    }
//
//    private fun noFiltering(): (Entry) -> Boolean {
//
//        return { true }
//    }
//
//}