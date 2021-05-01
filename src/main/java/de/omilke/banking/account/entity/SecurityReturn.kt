package de.omilke.banking.account.entity

import java.math.BigDecimal
import java.math.RoundingMode

class SecurityReturn(security: Security, currentPrice: BigDecimal) {

    val changeInValue: BigDecimal
    val relativeChange: BigDecimal

    init {

        val totalInvestedAmount = security.totalInvestedAmount

        val currentValue = security
                .totalCount.toBigDecimal()
                .multiply(currentPrice)

        changeInValue = totalInvestedAmount + currentValue
        relativeChange = changeInValue.divide(totalInvestedAmount.negate(), 10, RoundingMode.HALF_UP)

        //TODO: add xirr
    }

}