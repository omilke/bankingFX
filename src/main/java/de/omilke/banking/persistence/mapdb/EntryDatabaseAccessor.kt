package de.omilke.banking.persistence.mapdb
//
//import de.omilke.banking.account.entity.Entry
//import org.apache.logging.log4j.Level
//import org.apache.logging.log4j.LogManager
//import org.mapdb.DBMaker.fileDB
//import java.io.File
//
//internal object EntryDatabaseAccessor {
//
//    private const val DB_FILE = "file.db"
//
//    private val LOGGER = LogManager.getLogger(EntryDatabaseAccessor::class.java)
//
//    internal fun purgeDatabase() {
//
//        LOGGER.log(Level.INFO, "Purging MapDB file '$DB_FILE'")
//
//        File(DB_FILE).delete()
//    }
//
//    private val db by lazy {
//        fileDB(DB_FILE)
//                .transactionEnable()
//                .fileMmapEnableIfSupported()
//                .make()
//    }
//
//    internal val entries: MutableSet<Entry> by lazy {
//        val entrySet: MutableSet<Entry> = db
//                .hashSet("entries")
//                .serializer(EntrySerializerForMapDb())
//                .createOrOpen()
//
//        LOGGER.log(Level.INFO, "Loaded ${entrySet.size} entries from the Database")
//
//        entrySet
//    }
//
//    internal fun commit() {
//
//        db.commit()
//    }
//}
//
//fun checkPersistenceLayerReadiness() {
//
//    val logger = LogManager.getLogger(EntryDatabaseAccessor::class)
//
//    try {
//        logger.log(Level.INFO, "starting persistence readiness check")
//
//        EntryDatabaseAccessor.entries
//
//        logger.log(Level.INFO, "completed persistence readiness check")
//    } catch (e: Exception) {
//
//        logger.log(Level.FATAL, "Persistence Layer cannot be initialized due to underlying exception:", e)
//        throw  Exception("banking.fx cannot be initialized due to underlying exception")
//    }
//}
//
//fun purgeStorage() {
//
//    EntryDatabaseAccessor.purgeDatabase()
//}
//
