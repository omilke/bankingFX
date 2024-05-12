package de.omilke.bankingfx.controls

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView
import de.omilke.banking.BankingConfigurator.configuredLocale
import de.omilke.banking.account.entity.EndOfMonthRecurrence
import de.omilke.banking.account.entity.RecurrenceStrategy
import de.omilke.banking.account.entity.StartOfMonthRecurrence
import de.omilke.bankingfx.UIConstants
import javafx.geometry.Pos
import javafx.scene.Node
import javafx.scene.control.Cell
import javafx.scene.control.Labeled
import javafx.scene.paint.Color
import javafx.scene.text.Text
import java.math.BigDecimal
import java.text.DecimalFormat
import java.time.LocalDate
import java.util.function.Supplier

object UIUtils {

    fun formatAmount(amount: BigDecimal?): String {

        return DecimalFormat.getCurrencyInstance(configuredLocale()).format(amount)
    }

    fun formatPercent(amount: BigDecimal?, digits: Int = 2): String {

        val percentInstance = DecimalFormat.getPercentInstance(configuredLocale())
        percentInstance.minimumFractionDigits = digits

        return percentInstance.format(amount)
    }

    fun formatEntryDate(entryDate: LocalDate): String {

        return entryDate.format(UIConstants.DATE_FORMATTER)
    }

    fun isPositive(amount: BigDecimal?): Boolean = amount != null && amount >= BigDecimal.ZERO

    fun formatAmount(labeled: Labeled, amount: BigDecimal?) {

        //reset style in any case to prevent potentially inconsistent styles
        labeled.styleClass.removeAll(UIConstants.ALIGN_RIGHT, UIConstants.POSITIVE, UIConstants.NEGATIVE)

        if (amount == null) {
            labeled.text = null
        } else {
            labeled.styleClass.add(UIConstants.ALIGN_RIGHT)

            if (isPositive(amount)) {
                labeled.styleClass.add(UIConstants.POSITIVE)
            } else {
                labeled.styleClass.add(UIConstants.NEGATIVE)
            }

            labeled.text = formatAmount(amount)
        }
    }

    /**
     * Formats a Labeled while adhering to TableCell.updateItem convention to reset the text / graphic / styles
     * when it essentially is an empty / non-visible cell.
     *
     * @param labeled The Labeled to format the the amount into.
     * @param amount  The amount to be formatted.
     * @param empty   TableCell.updateItem empty flag.
     */
    fun formatAmount(labeled: Labeled, amount: BigDecimal?, empty: Boolean) {

        //reset style in any case to prevent potentially inconsistent styles
        labeled.styleClass.removeAll(UIConstants.ALIGN_RIGHT, UIConstants.POSITIVE, UIConstants.NEGATIVE)

        if (empty || amount == null) {
            labeled.text = null
        } else {
            labeled.styleClass.add(UIConstants.ALIGN_RIGHT)
            labeled.text = formatAmount(amount)

            if (amount >= BigDecimal.ZERO) {
                labeled.styleClass.add(UIConstants.POSITIVE)
            } else {
                labeled.styleClass.add(UIConstants.NEGATIVE)
            }
        }
    }

    fun applyStyleClass(styled: Node, lazyCondition: Supplier<Boolean>, styleClass: String?) {

        applyStyleClass(styled, styled, false, lazyCondition, styleClass)
    }

    fun applyStyleClass(styled: Node, item: Any?, empty: Boolean, lazyCondition: Supplier<Boolean>, styleClass: String?) {

        if (empty || item == null || !lazyCondition.get()) {
            styled.styleClass.remove(styleClass)
        } else {
            styled.styleClass.add(styleClass)
        }
    }

    fun formatDate(cell: Cell<LocalDate?>, item: LocalDate?, empty: Boolean) {

        if (item == null || empty) {
            cell.setText(null)
        } else {
            cell.text = formatEntryDate(item)
            cell.setAlignment(Pos.CENTER)
        }
    }

    fun getIconWithColor(icon: FontAwesomeIcon, color: Color): Text {

        val iconView = getIcon(icon)
        iconView.fill = color

        return iconView
    }

    fun getIcon(icon: FontAwesomeIcon): Text {
        return FontAwesomeIconView(icon)
    }

    fun formatRecurrenceStrategy(strategy: RecurrenceStrategy?): String {

        return when (strategy) {
            is EndOfMonthRecurrence -> "End of Month"
            is StartOfMonthRecurrence -> "Start of Month"
            else -> "unknown strategy"
        }
    }

}