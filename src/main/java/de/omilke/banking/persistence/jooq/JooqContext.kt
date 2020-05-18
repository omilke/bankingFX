package de.omilke.banking.persistence.jooq

import org.apache.logging.log4j.Level
import org.apache.logging.log4j.LogManager
import org.jooq.DSLContext
import org.jooq.SQLDialect
import org.jooq.impl.DSL
import java.sql.Connection
import java.sql.DriverManager

class JooqContext(
        private val userName: String,
        private val password: String,
        private val url: String,
        private val dialect: SQLDialect
) {

    /**
     * Provides the context that will be used to perform DB operations with JOOQ.
     */
    internal val context: DSLContext by lazy {

        DSL.using(connection, dialect)
    }

    private val connection: Connection by lazy {
        LogManager.getLogger(JooqContext::class.java).log(Level.INFO, "Creating connection using '{}' with user '{}' and dialect '{}'", url, userName, dialect)

        DriverManager.getConnection(url, userName, password)
    }

    internal fun commit() {

        connection.commit()
    }

    companion object {

        internal val DEFAULT_CONTEXT by lazy {
            JooqContext("user", "password", "jdbc:h2:./bankingfx.h2", SQLDialect.H2)
        }
    }
}
