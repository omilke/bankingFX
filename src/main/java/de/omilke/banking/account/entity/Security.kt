package de.omilke.banking.account.entity

import java.math.BigDecimal
import java.time.LocalDate

data class Security(
        var isin: String,
        var name: String,
        var type: SecurityType,
        var referenceTicker: String,
        var sellProvision: BigDecimal?
) : Comparable<Security> {

    private val _transactions = ArrayList<SecurityTransaction>()

    fun addTransaction(date: LocalDate, type: TransactionType, count: Int, price: BigDecimal, provision: BigDecimal) {

        _transactions.add(SecurityTransaction(this, date, type, count, price, provision))

        _transactions.sort()
    }

    val transactions
        get() = ArrayList(_transactions)

    val totalInvestedAmount: BigDecimal
        get() = transactions
                .map(SecurityTransaction::totalTransactionAmount)
                .reduce(BigDecimal::add)

    val isClosedPosition
        get() = totalCount == 0;

    val totalCount
        get() = transactions
                .filter { it.type == TransactionType.BUY || it.type == TransactionType.SELL }
                .map { if (it.type == TransactionType.SELL) -it.count else it.count }
                .sum()

    fun getCurrentValue(currentPrice: BigDecimal): BigDecimal {

        return currentPrice.multiply(BigDecimal(totalCount))
    }

    override fun compareTo(other: Security): Int {
        return compareValuesBy(
                this,
                other,
                compareBy(Security::type)
                        .thenBy(Security::isClosedPosition)
                        .thenBy(Security::totalInvestedAmount),
                { it })
    }

}
