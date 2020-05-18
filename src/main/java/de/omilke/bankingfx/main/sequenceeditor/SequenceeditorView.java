package de.omilke.bankingfx.main.sequenceeditor;

import de.omilke.banking.account.entity.EntrySequence;
import de.omilke.bankingfx.main.sequenceeditor.model.EntryOrderSetting;
import de.saxsys.mvvmfx.FluentViewLoader;
import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.ViewTuple;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.Optional;

public class SequenceeditorView implements FxmlView<SequenceeditorModel> {

    @FXML
    DialogPane dialog;

    @FXML
    ChoiceBox<EntrySequence> sequence;

    @FXML
    TextField order;

    public void initialize() {

        initSequence(this.sequence);
        initOrder(this.order);

    }

    private void initSequence(final ChoiceBox<EntrySequence> choiceBox) {

        choiceBox.getItems().clear();
        choiceBox.getItems().addAll(EntrySequence.values());

        choiceBox.setValue(EntrySequence.REGULAR);
    }

    private void initOrder(final TextField textfield) {

        textfield.setText("0");
    }

    private EntryOrderSetting provide(ButtonType buttonType) {

        EntryOrderSetting result;

        if (buttonType == ButtonType.APPLY) {
            try {
                result = getResult();
            } catch (final InvalidEntryOrderException e) {
                result = null;
            }
        } else {
            result = null;
        }

        return result;
    }

    private EntryOrderSetting getResult() throws InvalidEntryOrderException {

        final String text = this.order.getText();

        try {
            final int orderIndex = Integer.parseInt(text);

            // exactly two digit
            if (orderIndex >= 0 && orderIndex < 100) {
                return new EntryOrderSetting(this.sequence.getValue(), orderIndex);
            } else {
                throw new InvalidEntryOrderException(orderIndex);
            }
        } catch (final NumberFormatException e) {
            throw new InvalidEntryOrderException(text);
        }

    }

    private void initValues(final EntrySequence sequence, final int orderIndex) {

        this.sequence.setValue(sequence);
        this.order.setText("" + orderIndex);

    }

    public static Optional<EntryOrderSetting> openView(final EntrySequence sequence, final int orderIndex) {

        ViewTuple<SequenceeditorView, SequenceeditorModel> viewTuple = FluentViewLoader.fxmlView(SequenceeditorView.class).load();
        SequenceeditorView view = viewTuple.getCodeBehind();
        view.initValues(sequence, orderIndex);

        final Dialog<EntryOrderSetting> dialog = new Dialog<>();
        dialog.setDialogPane((DialogPane) viewTuple.getView());
        dialog.setResultConverter(view::provide);

        return dialog.showAndWait();
    }

}
