package de.omilke.banking.account.entity

import java.math.BigDecimal
import java.time.LocalDate

data class SecurityTransaction(
        var security: Security,
        var date: LocalDate,
        var type: TransactionType,
        var count: Int,
        var price: BigDecimal,
        var provision: BigDecimal
) : Comparable<SecurityTransaction> {

    val totalTransactionAmount: BigDecimal
        get() = price
                .multiply(BigDecimal(count))
                .multiply(transactionTypeFactor)
                .add(provision)

    /**
     * determines the signum of the actual transaction value, i. e. cash inflow vs. cash outflow.
     *
     * In that instance, buying is cash outflow, whereas selling and dividends are cash inflow.
     */
    private val transactionTypeFactor: BigDecimal = run {
        if (type == TransactionType.BUY)
            BigDecimal(-1)
        else {
            BigDecimal.ONE
        }
    }

    override fun compareTo(other: SecurityTransaction): Int {
        return compareValuesBy(
                this,
                other,
                compareBy(SecurityTransaction::date),
                { it })
    }
}
