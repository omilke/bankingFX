package de.omilke.bankingfx.report.audit

import de.omilke.banking.BankingConfigurator.configuredLocale
import de.omilke.banking.account.entity.Entry
import de.omilke.banking.persistence.PersistenceServiceProvider.persistenceService
import de.omilke.bankingfx.UIConstants
import de.saxsys.mvvmfx.FxmlView
import javafx.fxml.FXML
import javafx.scene.control.DatePicker
import javafx.scene.control.TextField
import java.math.BigDecimal
import java.text.DecimalFormat
import java.text.NumberFormat
import java.text.ParseException
import java.time.LocalDate

class AuditView : FxmlView<AuditModel> {

    private val er = persistenceService.entryRepository

    @FXML
    lateinit var datePicker: DatePicker

    @FXML
    lateinit var actualBalance: TextField

    @FXML
    lateinit var correction: TextField

    @FXML
    lateinit var regularBalance: TextField

    @FXML
    lateinit var savings: TextField

    @FXML
    lateinit var delta: TextField

    var locale = configuredLocale()

    fun initialize() {

        datePicker.value = LocalDate.now()

        loadBalance()
    }

    @FXML
    fun loadBalance() {

        val allEntriesInRange: List<Entry> = er.findAllEntriesBetweenWithCategoryName(null, datePicker.value, null)

        val totalBalance = allEntriesInRange.sumOf(Entry::amount)

        formatTextField(regularBalance, totalBalance)

        val savings = allEntriesInRange
                .filter(Entry::isSaving)
                .sumOf(Entry::amount)
                .negate()

        formatTextField(this.savings, savings)

        setDelta()
    }

    private fun formatTextField(text: TextField, value: BigDecimal) {

        text.text = NumberFormat.getCurrencyInstance(locale).format(value)

        if (value >= BigDecimal.ZERO) {
            text.styleClass.remove(UIConstants.NEGATIVE)
            text.styleClass.add(UIConstants.POSITIVE)
        } else {
            text.styleClass.remove(UIConstants.POSITIVE)
            text.styleClass.add(UIConstants.NEGATIVE)
        }
    }

    @FXML
    fun setDelta() {

        val regularBalance = valueOf(regularBalance.text)
        val savings = valueOf(savings.text)

        val actualBalance = valueOf(actualBalance.text)
        val corrections = valueOf(correction.text)

        val delta = calculateDelta(regularBalance, savings, actualBalance, corrections)

        formatTextField(this.delta, delta)
    }

    fun calculateDelta(balance: BigDecimal, savings: BigDecimal?, actualBalance: BigDecimal, corrections: BigDecimal?): BigDecimal {

        val totalRegularBalance = balance.add(savings)
        val totalActualBalance = actualBalance.add(corrections)

        return totalActualBalance.subtract(totalRegularBalance)
    }

    /**
     * Returns a number from a String, even when there is a currency symbol attached, e. g. `123,22 €`.
     *
     *
     * Returns [BigDecimal.ZERO] in case the String is `null`, empty or cannot be parsed.
     */
    fun valueOf(text: String?): BigDecimal {

        return when {
            text.isNullOrEmpty() -> BigDecimal.ZERO
            else -> try {
                val df = DecimalFormat.getNumberInstance(locale) as DecimalFormat
                df.isParseBigDecimal = true

                df.parse(text) as BigDecimal
            } catch (e: ParseException) {
                BigDecimal.ZERO
            }
        }
    }
}