package de.omilke.bankingfx.report.audit;

import de.omilke.banking.BankingConfigurator;
import de.omilke.banking.account.entity.Entry;
import de.omilke.banking.account.entity.EntryRepository;
import de.omilke.banking.persistence.PersistenceServiceProvider;
import de.omilke.bankingfx.UIConstants;
import de.saxsys.mvvmfx.FxmlView;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

public class AuditView implements FxmlView<AuditModel> {

    private EntryRepository er = PersistenceServiceProvider.INSTANCE.getPersistenceService().getEntryRepository();

    @FXML
    DatePicker datePicker;

    @FXML
    TextField actualBalance;
    @FXML
    TextField correction;
    @FXML
    TextField regularBalance;
    @FXML
    TextField savings;
    @FXML
    TextField delta;

    Locale locale = BankingConfigurator.INSTANCE.configuredLocale();

    public void initialize() {

        this.datePicker.setValue(LocalDate.now());

        this.loadBalance();
    }

    @FXML
    public void loadBalance() {

        final List<Entry> allEntriesInRange = this.er.findAllEntriesBetweenWithCategoryName(null, this.datePicker.getValue(), null);

        // @formatter:off
        final BigDecimal totalBalance = allEntriesInRange
                .stream()
                .map(Entry::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        // @formatter:on

        this.formatTextField(this.regularBalance, totalBalance);

        // @formatter:off
        final BigDecimal savings = allEntriesInRange
                .stream()
                .filter(Entry::isSaving)
                .map(Entry::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .multiply(new BigDecimal(-1));
        // @formatter:on

        formatTextField(this.savings, savings);

        this.setDelta();
    }

    private void formatTextField(final TextField textfield, final BigDecimal value) {

        textfield.setText(NumberFormat.getCurrencyInstance(locale).format(value));

        if (value.compareTo(BigDecimal.ZERO) >= 0) {
            textfield.getStyleClass().remove(UIConstants.NEGATIVE);
            textfield.getStyleClass().add(UIConstants.POSITIVE);
        } else {
            textfield.getStyleClass().remove(UIConstants.POSITIVE);
            textfield.getStyleClass().add(UIConstants.NEGATIVE);
        }
    }

    @FXML
    public void setDelta() {

        final BigDecimal regularBalance = valueOf(this.regularBalance.getText());
        final BigDecimal savings = valueOf(this.savings.getText());

        final BigDecimal actualBalance = valueOf(this.actualBalance.getText());
        final BigDecimal corrections = valueOf(this.correction.getText());

        final BigDecimal delta = calculateDelta(regularBalance, savings, actualBalance, corrections);

        this.formatTextField(this.delta, delta);
    }

    BigDecimal calculateDelta(final BigDecimal balance, final BigDecimal savings, final BigDecimal actualBalance, final BigDecimal corrections) {

        final BigDecimal totalRegularBalance = balance.add(savings);
        final BigDecimal totalActualBalance = actualBalance.add(corrections);

        final BigDecimal delta = totalActualBalance.subtract(totalRegularBalance);
        return delta;
    }

    /**
     * Liefert die Zahl aus einem Text. Liefert {@link BigDecimal#ZERO}, wenn der String <code>null</code> oder leer ist. Akzeptiert auch
     * nachgestellte Währungszeichen, bspw. <code>123,22 €</code>
     */
    BigDecimal valueOf(final String text) {

        if (text == null || text.isEmpty()) {
            return BigDecimal.ZERO;
        } else {
            try {
                final DecimalFormat df = (DecimalFormat) DecimalFormat.getNumberInstance(locale);
                df.setParseBigDecimal(true);

                return (BigDecimal) df.parse(text);
            } catch (final ParseException e) {
                return BigDecimal.ZERO;
            }

        }
    }

}
