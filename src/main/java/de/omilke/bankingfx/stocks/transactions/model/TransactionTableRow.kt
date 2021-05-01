package de.omilke.bankingfx.stocks.transactions.model

import de.omilke.banking.account.entity.Security
import de.omilke.banking.account.entity.SecurityReturn
import de.omilke.banking.account.entity.SecurityTransaction
import de.omilke.bankingfx.UIConstants
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import javafx.beans.property.StringProperty
import java.math.BigDecimal

class TransactionTableRow {

    val description: StringProperty
    val count: SimpleObjectProperty<Int?>
    val price: SimpleObjectProperty<BigDecimal?>
    val provision: SimpleObjectProperty<BigDecimal?>
    val totalAmount: SimpleObjectProperty<BigDecimal?>
    val relativeChange: SimpleObjectProperty<BigDecimal?>

    val parentElement: Boolean
    val aggregateElement: Boolean

    /**
     * this is only intended for the non-displayed root row element.
     */
    constructor() :
            this(false, false, "", null, null, null, null, null)

    constructor(security: Security) :
            this(true, false, securityDisplayName(security), null, null, null, null, null)

    constructor(transaction: SecurityTransaction) :
            this(false, false, transaction.date.format(UIConstants.DATE_FORMATTER), transaction.count, transaction.price, transaction.provision, transaction.totalTransactionAmount, null)

    constructor(description: String, totalCount: Int, amount: BigDecimal) :
            this(false, true, description, totalCount, null, null, amount, null)

    constructor(description: String, securityReturn: SecurityReturn) :
            this(false, true, description, null, null, null, securityReturn.changeInValue, securityReturn.relativeChange)

    private constructor(parent: Boolean, aggregate: Boolean, description: String, count: Int?, price: BigDecimal?, provision: BigDecimal?, totalAmount: BigDecimal?, relativeChange: BigDecimal?) {

        this.description = SimpleStringProperty(description)
        this.count = SimpleObjectProperty<Int?>(count)
        this.price = SimpleObjectProperty(price)
        this.provision = SimpleObjectProperty(provision)
        this.totalAmount = SimpleObjectProperty(totalAmount)
        this.relativeChange = SimpleObjectProperty(relativeChange)

        this.parentElement = parent
        this.aggregateElement = aggregate

    }

}

private fun securityDisplayName(security: Security): String {

    var result = security.name

    if (security.isClosedPosition) {
        result += " [closed]"
    }

    return result
}