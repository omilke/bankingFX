package de.omilke.bankingfx.report.savings;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.omilke.bankingfx.UIConstants;
import de.omilke.bankingfx.controls.AmountTreeTableCell;
import de.omilke.bankingfx.controls.UIUtils;
import de.omilke.bankingfx.report.savings.model.Category;
import de.omilke.bankingfx.report.savings.model.Entry;
import de.saxsys.mvvmfx.Context;
import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectContext;
import de.saxsys.mvvmfx.InjectViewModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.math.BigDecimal;
import java.time.LocalDate;

public class SavingsView implements FxmlView<SavingsModel> {

    @FXML
    CheckBox balancedCategories;

    @FXML
    TreeTableView<Entry> savingsTable;

    @InjectViewModel
    private SavingsModel viewModel;

    @InjectContext
    private Context context;

    public void initialize() {

        savingsTable.setRoot(new TreeItem<>(new Entry()));
        savingsTable.setColumnResizePolicy(param -> true);
        savingsTable.setShowRoot(false);

        this.setColumns();

        this.displayModel(balancedCategories.isSelected());
    }

    private void displayModel(boolean hideBalancedCategories) {

        savingsTable.getRoot().getChildren().clear();

        for (final Category current : viewModel.getCategories()) {

            if (displayCategory(current, hideBalancedCategories)) {

                TreeItem<Entry> currentRoot = new TreeItem<>(new Entry(current.getName(), current.getSum()));
                this.savingsTable.getRoot().getChildren().add(currentRoot);

                currentRoot.setExpanded(false);

                for (Entry entry : current.getEntries()) {
                    currentRoot.getChildren().add(new TreeItem<>(entry));
                }
            }

        }
    }

    private boolean displayCategory(Category current, boolean hideBalancedCategories) {

        if (hideBalancedCategories && current.getSum().compareTo(BigDecimal.ZERO) == 0) {
            return false;
        } else {
            return true;
        }
    }

    private void setColumns() {

        final TreeTableColumn<Entry, String> groupLabelColumn = new TreeTableColumn<>("Category");
        groupLabelColumn.setPrefWidth(UIConstants.CATEGORY_WIDTH);
        groupLabelColumn.setCellValueFactory(param -> param.getValue().getValue().groupLabelProperty());

        final TreeTableColumn<Entry, LocalDate> dateColumn = new TreeTableColumn<>("Date");
        dateColumn.setPrefWidth(UIConstants.DATE_WIDTH);
        dateColumn.getStyleClass().add("center");
        dateColumn.setCellValueFactory(param -> param.getValue().getValue().entryDateProperty());
        dateColumn.setCellFactory(column -> new TreeTableCell<>() {

            @Override
            protected void updateItem(final LocalDate item, final boolean empty) {

                super.updateItem(item, empty);

                if (item == null || empty) {
                    setText(null);
                } else {
                    setText(item.format(UIConstants.DATE_FORMATTER));
                }

            }
        });

        final TreeTableColumn<Entry, BigDecimal> amountColumn = new TreeTableColumn<>("Amount");
        amountColumn.setPrefWidth(UIConstants.AMOUNT_WIDTH);
        amountColumn.getStyleClass().add("right");
        amountColumn.setCellValueFactory(param -> param.getValue().getValue().amountProperty());
        amountColumn.setCellFactory(column -> new AmountTreeTableCell<>());

        final TreeTableColumn<Entry, Boolean> savingColumn = new TreeTableColumn<>("Saving");
        savingColumn.setPrefWidth(UIConstants.SAVING_WIDTH);
        savingColumn.getStyleClass().add("center");
        savingColumn.setCellValueFactory(param -> param.getValue().getValue().savingProperty());
        savingColumn.setCellFactory(column -> new TreeTableCell<>() {

            @Override
            protected void updateItem(final Boolean item, final boolean empty) {

                super.updateItem(item, empty);

                final Entry rowItem = getTreeTableRow().getItem();
                if (!empty && item != null && item && rowItem != null) {

                    final BigDecimal amount = rowItem.getAmount();

                    final Text icon;

                    if (amount.compareTo(BigDecimal.ZERO) < 0) {
                        icon = UIUtils.getIconWithColor(FontAwesomeIcon.DOWNLOAD, Color.GREEN);
                    } else {
                        icon = UIUtils.getIconWithColor(FontAwesomeIcon.UPLOAD, Color.RED);
                    }

                    setGraphic(icon);

                } else {
                    setGraphic(null);
                }

            }
        });

        final TreeTableColumn<Entry, String> commentColumn = new TreeTableColumn<>("Comment");
        commentColumn.setPrefWidth(UIConstants.COMMENT_WIDTH);
        commentColumn.setCellValueFactory(param -> param.getValue().getValue().commentProperty());

        savingsTable.getColumns().clear();
        savingsTable.getColumns().add(groupLabelColumn);
        savingsTable.getColumns().add(dateColumn);
        savingsTable.getColumns().add(amountColumn);
        savingsTable.getColumns().add(savingColumn);
        savingsTable.getColumns().add(commentColumn);
    }

    @FXML
    public void updateModel(ActionEvent actionEvent) {

        CheckBox source = (CheckBox) actionEvent.getSource();
        displayModel(source.isSelected());
    }

}
