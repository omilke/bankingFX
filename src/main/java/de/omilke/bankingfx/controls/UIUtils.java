package de.omilke.bankingfx.controls;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import de.omilke.banking.BankingConfigurator;
import de.omilke.banking.account.entity.EndOfMonthRecurrence;
import de.omilke.banking.account.entity.RecurrenceStrategy;
import de.omilke.banking.account.entity.StartOfMonthRecurrence;
import de.omilke.bankingfx.UIConstants;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Cell;
import javafx.scene.control.Labeled;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.function.Supplier;

/**
 * Created by Olli on 25.05.2017.
 */
public class UIUtils {

    public static String formatAmount(BigDecimal amount) {

        return DecimalFormat.getCurrencyInstance(BankingConfigurator.INSTANCE.configuredLocale()).format(amount);
    }

    public static void formatAmount(Labeled labeled, BigDecimal amount) {

        formatAmount(labeled, amount, false);
    }

    /**
     * Formats a Labeled while adhering to TableCell.updateItem convention to reset the text / graphic / styles
     * when it essentially is an empty / non-visible cell.
     *
     * @param labeled The Labeled to format the the amount into.
     * @param amount  The amount to be formatted.
     * @param empty   TableCell.updateItem empty flag.
     */
    public static void formatAmount(Labeled labeled, BigDecimal amount, boolean empty) {

        //reset style in any case to prevent potentially inconsistent styles
        labeled.getStyleClass().clear();

        if (empty || amount == null) {
            labeled.setText(null);
        } else {
            labeled.getStyleClass().add(UIConstants.ALIGN_RIGHT);

            labeled.setText(formatAmount(amount));
            if (amount.compareTo(BigDecimal.ZERO) >= 0) {
                labeled.getStyleClass().add(UIConstants.POSITIVE);
            } else {
                labeled.getStyleClass().add(UIConstants.NEGATIVE);
            }
        }
    }

    public static void applyStyleClass(Node styled, Supplier<Boolean> lazyCondition, String styleClass) {

        applyStyleClass(styled, styled, false, lazyCondition, styleClass);
    }

    public static void applyStyleClass(Node styled, Object item, boolean empty, Supplier<Boolean> lazyCondition, String styleClass) {

        if (empty || item == null || !lazyCondition.get()) {
            styled.getStyleClass().remove(styleClass);
        } else {
            styled.getStyleClass().add(styleClass);
        }

    }

    public static void formatDate(Cell<LocalDate> cell, LocalDate item, boolean empty) {

        if (item == null || empty) {
            cell.setText(null);
        } else {
            cell.setText(item.format(UIConstants.DATE_FORMATTER));
            cell.setAlignment(Pos.CENTER);
        }

    }

    public static Text getIconWithColor(FontAwesomeIcon icon, Color color) {

        Text iconView = getIcon(icon);
        iconView.setFill(color);

        return iconView;
    }

    public static Text getIcon(FontAwesomeIcon icon) {

        return new FontAwesomeIconView(icon);
    }

    public static String formatRecurrenceStrategy(RecurrenceStrategy strategy) {

        if (strategy instanceof EndOfMonthRecurrence) {
            return "End of Month";
        } else if (strategy instanceof StartOfMonthRecurrence) {
            return "Start of Month";
        } else {
            return "unknown strategy";
        }
    }
}
