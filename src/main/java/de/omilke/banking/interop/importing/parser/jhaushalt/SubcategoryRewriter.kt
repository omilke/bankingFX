package de.omilke.banking.interop.importing.parser.jhaushalt

import org.apache.logging.log4j.Level
import org.apache.logging.log4j.LogManager
import java.util.*

/**
 * Class to provide a mapping configuration for subcategories based the entry's comment.
 *
 */
class SubcategoryRewriter(configItems: List<String>) {

    private val mapping = LinkedHashSet<SubcategoryConfiguration>()

    init {

        for (s in configItems) {
            val result = s.split(";")

            if (result.size != 3) {
                LOGGER.log(Level.WARN, "Skipping invalid Subcategory-Rewriting rule {}", s)
            } else {
                val configuration = SubcategoryConfiguration(result[0], result[1], result[2])
                mapping.add(configuration)
                LOGGER.log(Level.TRACE, "Rule loaded {}", configuration)
            }
        }
    }

    val mappingCount: Int
        get() = mapping.size

    fun map(category: String, comment: String): String {

        for (configuration in mapping) {
            if (configuration.originalCategory.equals(category, true) && comment.contains(configuration.keyword)) {
                return String.format("%s:%s", category, configuration.subCategory)
            }
        }

        return category
    }

    internal data class SubcategoryConfiguration(internal val originalCategory: String, internal val keyword: String, internal val subCategory: String)

    companion object {

        private val LOGGER = LogManager.getLogger(SubcategoryRewriter::class.java)
    }

}
