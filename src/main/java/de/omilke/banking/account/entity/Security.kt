package de.omilke.banking.account.entity

import java.math.BigDecimal

data class Security(
        var isin: String,
        var name: String,
        var referenceTicker: String,
        var sellProvision: BigDecimal?
)