//package de.omilke.banking.storage
//
//import de.omilke.banking.BankingConfigurator
//import de.omilke.banking.persistence.PersistenceProvider
//import org.apache.logging.log4j.Level
//import org.apache.logging.log4j.LogManager
//import java.util.*
//import javax.persistence.EntityManager
//import javax.persistence.Persistence
//
//object Jpa {
//
//    private const val SCHEMA_GENERATION_PROPERTY = "javax.persistence.schema-generation.database.action"
//
//    val entityManager: EntityManager by lazy {
//        //lazily initialize the EM in order to prevent Exception in Initializer Error
//        val props = Properties()
//
//        if (BankingConfigurator.importSourceConfiguration.isPresent) {
//            props.setProperty(SCHEMA_GENERATION_PROPERTY, "drop-and-create")
//        }
//
//        val emf = Persistence.createEntityManagerFactory("bankingFX", props)
//
//        emf.createEntityManager()
//    }
//
//}
//
//fun checkPersistenceLayerReadiness() {
//
//    val logger = LogManager.getLogger(Jpa::class)
//
//    try {
//        logger.log(Level.INFO, "starting persistence readiness check")
//
//        PersistenceProvider.getEntryRepository().findAllEntries()
//
//        logger.log(Level.INFO, "completed persistence readiness check")
//    } catch (e: Exception) {
//
//        logger.log(Level.FATAL, "Persistence Layer cannot be initialized due to underlying exception:", e)
//        throw  Exception("banking.fx cannot be initialized due to underlying exception")
//    }
//}
