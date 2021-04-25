package de.omilke.banking.account.entity

import java.math.BigDecimal
import java.time.LocalDate

data class SecurityTransaction(
    var security: Security,
    var count: Int,
    var price: BigDecimal,
    var provision: BigDecimal,
    var date: LocalDate
) {

    fun totalTransactionAmount(): BigDecimal {
        return price
            .multiply(BigDecimal(count))
            .multiply(BigDecimal.ONE + provision);
    }
}