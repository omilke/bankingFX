package de.omilke.banking

import org.apache.logging.log4j.Level
import org.apache.logging.log4j.LogManager
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.net.URISyntaxException
import java.nio.file.Files
import java.nio.file.Path
import java.util.*
import kotlin.collections.HashMap

object BankingConfigurator {

    private val LOGGER = LogManager.getLogger(BankingConfigurator::class.java)

    private const val PROPERTIES_FILE_CONFIG_KEY = "bankingfx.config"
    private const val PROPERTIES_PATH_CONFIG_KEY = "bankingfx.configPath"
    private const val PROPERTIES_DEFAULT_FILENAME = "bankingfx.properties"

    private val properties = loadConfigurationFrom()

    private fun loadConfigurationFrom(): Properties {

        val file = getConfigurationFile()
        if (file.exists()) {

            LOGGER.log(Level.INFO, "Using configuration file: {} ", file.path)

            return try {
                val result = Properties()
                result.load(FileInputStream(file))

                for ((key, value) in result.entries) {
                    LOGGER.log(Level.DEBUG, "\t{}={}", key, value)
                }

                result
            } catch (ex: IOException) {
                LOGGER.log(Level.ERROR, "Error reading configuration file. Resuming with default configuration.", ex)

                Properties()
            }
        } else {
            LOGGER.log(Level.INFO, "Configuration file does not exist. Resuming with default configuration,")

            return Properties()
        }
    }

    private fun getConfigurationFile(): File {

        var propertyFilePath = readSystemStringProperty(PROPERTIES_FILE_CONFIG_KEY)
        if (!propertyFilePath.isPresent) {
            //in case property is not set in system-config, try another layer of indirection
            val propertyPathConfigFile = readSystemStringProperty(PROPERTIES_PATH_CONFIG_KEY)
            if (propertyPathConfigFile.isPresent) {
                LOGGER.log(Level.INFO, "Reading property file path: {} ", propertyPathConfigFile.get())

                propertyFilePath = Optional.of(Files.readString(Path.of(propertyPathConfigFile.get())))
            }
        }

        return if (propertyFilePath.isPresent) {
            //use value of system property if specified
            File(propertyFilePath.get())
        } else {
            //use default if no value is specified
            File(getDefaultConfigurationDirectory(), PROPERTIES_DEFAULT_FILENAME)
        }

    }

    private fun getDefaultConfigurationDirectory(): File {

        return try {
            val url = BankingConfigurator::class.java.protectionDomain.codeSource.location

            File(url.toURI())
        } catch (e: URISyntaxException) {
            val userHome = System.getProperty("user.home")

            LOGGER.log(Level.WARN, "Path of the executable not not be read, using '{}' instead.", userHome, e)

            File(userHome)
        }

    }

    fun configuredLocale(): Locale {

        return Locale.GERMANY
    }

    private fun readSystemStringProperty(configKey: String): Optional<String> {

        return Optional.ofNullable(System.getProperty(configKey, null))
    }

    /**
     * Looks up the specified property key and returns *true* if and only if the value
     * is present and equals "true".
     *
     * Returns *true* if the value is not set or not equal to "true".
     */
    fun isPropertyEnabled(value: String): Boolean {

        return findPropertyConfiguration(value)?.toBoolean() ?: false
    }

    fun findPropertyConfiguration(value: String): String? {

        return properties[value] as String?
    }

    /**
     * Provides the configuration of the application with respect to the specified property keys. Keys that have not been set are provided a value of null.
     */
    fun getConfigurationFor(configKeys: List<String>): Map<String, String?> {

        val configuration = HashMap<String, String?>()

        for (configKey in configKeys) {

            val value = findPropertyConfiguration(configKey)
            configuration[configKey] = value
        }

        return configuration

    }
}