package de.omilke.banking.interop.importing.parser.jhaushalt

import de.omilke.banking.account.entity.EntrySequence
import org.apache.logging.log4j.Level
import org.apache.logging.log4j.LogManager
import java.util.*

/**
 * Class to provide a mapping configuration for OrderSequence and OrderIndex based the entry's comment.
 *
 * @author Oliver Milke
 * @since 20.02.2017
 */
class OrderRewriter(configItems: List<String>) {

    private val mapping = HashMap<String, OrderSetting>()

    val mappingCount: Int
        get() = mapping.size

    init {

        for (s in configItems) {
            val split = s.split(';')

            if (split.size != 3) {
                logInvalidLine(s)
                continue
            }

            val entrySequence = EntrySequence.of(split[1])
            if (entrySequence == null) {
                logInvalidLine(s)
                continue
            }

            try {
                val orderIndex = Integer.valueOf(split[2])
                mapping[split[0]] = OrderSetting(entrySequence, orderIndex)
            } catch (e: NumberFormatException) {
                logInvalidLine(s)
            }

        }
    }

    private fun logInvalidLine(s: String) {

        LOGGER.log(Level.WARN, "Skipping invalid Order-Rewriting rule {}", s)
    }

    fun map(original: String): OrderSetting {

        val orderSetting = mapping[original]

        return orderSetting ?: OrderSetting(EntrySequence.REGULAR, 0)

    }

    /**
     * Container for holding an EntrySequence and an OrderIndex.
     *
     * @author Oliver Milke
     * @since 20.02.2017
     */
    data class OrderSetting internal constructor(val entrySequence: EntrySequence, val orderIndex: Int)

    companion object {

        private val LOGGER = LogManager.getLogger(OrderRewriter::class.java)
    }
}

