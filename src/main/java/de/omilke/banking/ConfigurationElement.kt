package de.omilke.banking

/**
 * Represents a certain configured value and its original configuration value provided as parameter.
 *
 * Example:
 * - Value: a stream
 * - Configuration: path to the resource provided as stream
 *
 * This allows the [BankingConfigurator] to resolve the properties while still maintaining information about the original value.
 *
 */
class ConfigurationElement<T> {

    val value: T?
    val configuration: String?

    constructor() {
        value = null
        configuration = null
    }

    constructor(value: T, configuration: String) {
        this.value = value
        this.configuration = configuration
    }

    val isPresent: Boolean
        get() = configuration != null

}
