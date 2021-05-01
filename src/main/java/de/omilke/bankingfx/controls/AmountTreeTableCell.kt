package de.omilke.bankingfx.controls

import java.math.BigDecimal

class AmountTreeTableCell<T> : PositiveNegativeTableCell<T, BigDecimal?>(
        UIUtils::isPositive,
        UIUtils::formatAmount
)


