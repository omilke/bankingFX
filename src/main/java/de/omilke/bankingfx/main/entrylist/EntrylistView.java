package de.omilke.bankingfx.main.entrylist;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.omilke.banking.account.entity.EntryRepository;
import de.omilke.banking.persistence.PersistenceServiceProvider;
import de.omilke.bankingfx.UIConstants;
import de.omilke.bankingfx.controls.AmountTreeTableCell;
import de.omilke.bankingfx.controls.ResizeableColumnCallback;
import de.omilke.bankingfx.controls.UIUtils;
import de.omilke.bankingfx.main.entrylist.model.Entry;
import de.omilke.bankingfx.main.entrylist.model.EntryOrder;
import de.saxsys.mvvmfx.FxmlView;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EntrylistView implements FxmlView<EntrylistModel> {

    private static final int MONTH_TO_EXPAND = 3;

    private EntryRepository er = PersistenceServiceProvider.INSTANCE.getPersistenceService().getEntryRepository();

    @FXML
    TextField filter;

    @FXML
    TreeTableView<Entry> entryTable;

    @FXML
    Label entryCountLabel;

    private List<Entry> entries = new ArrayList<>();

    public void initialize() {

        entryTable.setRoot(new TreeItem<>(new Entry(null, null, 0, null, false, null, null)));
        entryTable.setColumnResizePolicy(new ResizeableColumnCallback<>());
        entryTable.setShowRoot(false);

        setColumns();

        refreshEntries();
    }

    public void refreshEntries() {

        this.entries = er
                .findAllEntries()
                .stream()
                .map(
                        current -> new Entry(
                                current.getEntryDate(),
                                current.getSequence(),
                                current.getOrderIndex(),
                                current.getAmount(),
                                current.isSaving(),
                                current.getCategory(),
                                current.getComment()))
                .collect(Collectors.toList());

        fillTable(entries);
    }

    private void fillTable(final List<Entry> entries) {

        this.entryTable.getRoot().getChildren().clear();

        LocalDate previous = null;
        TreeItem<Entry> currentRoot = new TreeItem<>();
        int i = 1;
        for (final Entry entry : entries) {

            final LocalDate key = getFirstOfMonth(entry.getEntryDate());

            if (!key.equals(previous)) {
                currentRoot = new TreeItem<>(new Entry(key));

                final boolean expanded;
                if (isInFutureMonth(key)) {
                    // keep future month collapsed...
                    expanded = false;
                } else {
                    // ... but only expand so many month
                    expanded = i++ <= MONTH_TO_EXPAND;
                }
                currentRoot.setExpanded(expanded);

                this.entryTable.getRoot().getChildren().add(currentRoot);

                previous = key;
            }

            currentRoot.getChildren().add(new TreeItem<>(entry));

        }

        this.entryCountLabel.setText(entries.size() + "");
    }

    boolean isInFutureMonth(final LocalDate key) {

        final LocalDate firstOfCurrentMonth = getFirstOfMonth(LocalDate.now());

        return getFirstOfMonth(key).isAfter(firstOfCurrentMonth);
    }

    LocalDate getFirstOfMonth(final LocalDate from) {

        return LocalDate.of(from.getYear(), from.getMonth(), 1);

    }

    private void setColumns() {

        final TreeTableColumn<Entry, LocalDate> dateColumn = new TreeTableColumn<>("Date");
        dateColumn.setPrefWidth(UIConstants.MONTH_WIDTH);
        dateColumn.setCellValueFactory(param -> param.getValue().getValue().entryDateProperty());
        dateColumn.setCellFactory(column -> new TreeTableCell<Entry, LocalDate>() {

            @Override
            protected void updateItem(final LocalDate item, final boolean empty) {

                super.updateItem(item, empty);

                final Entry rowItem = getTreeTableRow().getItem();
                if (item == null || empty || rowItem == null) {
                    setText(null);
                } else {
                    final boolean isGroupElement = rowItem.isGroupElement();

                    if (isGroupElement) {
                        setText(item.format(UIConstants.MONTH_NAME_FORMATTER));
                        setAlignment(Pos.CENTER_LEFT);
                    } else {
                        setText(item.format(UIConstants.DATE_FORMATTER));
                        setAlignment(Pos.CENTER);
                    }

                }
            }
        });

        final TreeTableColumn<Entry, EntryOrder> sequenceColumn = new TreeTableColumn<>("");
        sequenceColumn.setPrefWidth(UIConstants.SEQUENCE_WIDTH);
        sequenceColumn.getStyleClass().add(UIConstants.ALIGN_CENTER);
        sequenceColumn.setCellValueFactory(param -> param.getValue().getValue().entryOrderProperty());
        sequenceColumn.setCellFactory(column -> new TreeTableCell<Entry, EntryOrder>() {

            @Override
            protected void updateItem(final EntryOrder item, final boolean empty) {

                super.updateItem(item, empty);

                final Entry rowItem = getTreeTableRow().getItem();
                if (item == null || empty || rowItem == null) {
                    setGraphic(null);
                } else {
                    final Text icon;

                    switch (item.getSequence()) {
                        case FIRST:
                            icon = UIUtils.INSTANCE.getIcon(FontAwesomeIcon.CHEVRON_CIRCLE_UP);
                            break;
                        case LAST:
                            icon = UIUtils.INSTANCE.getIcon(FontAwesomeIcon.CHEVRON_CIRCLE_DOWN);
                            break;
                        default:
                            icon = null;
                            break;
                    }

                    setGraphic(icon);
                }
            }
        });

        final TreeTableColumn<Entry, BigDecimal> amountColumn = new TreeTableColumn<>("Amount");
        amountColumn.setPrefWidth(UIConstants.AMOUNT_WIDTH);
        amountColumn.setCellValueFactory(param -> param.getValue().getValue().amountProperty());
        amountColumn.setCellFactory(column -> new AmountTreeTableCell<>());

        final TreeTableColumn<Entry, Boolean> savingColumn = new TreeTableColumn<>("Saving");
        savingColumn.setPrefWidth(UIConstants.SAVING_WIDTH);
        savingColumn.getStyleClass().add("center");
        savingColumn.setCellValueFactory(param -> param.getValue().getValue().savingProperty());
        savingColumn.setCellFactory(column -> new TreeTableCell<Entry, Boolean>() {

            @Override
            protected void updateItem(final Boolean item, final boolean empty) {

                super.updateItem(item, empty);

                final Entry rowItem = getTreeTableRow().getItem();
                if (!empty && item != null && item && rowItem != null) {

                    final BigDecimal amount = rowItem.getAmount();

                    final Node icon;

                    if (amount.compareTo(BigDecimal.ZERO) < 0) {
                        icon = UIUtils.INSTANCE.getIconWithColor(FontAwesomeIcon.DOWNLOAD, Color.GREEN);
                    } else {
                        icon = UIUtils.INSTANCE.getIconWithColor(FontAwesomeIcon.UPLOAD, Color.RED);
                    }

                    setGraphic(icon);

                } else {
                    setGraphic(null);
                }

            }
        });

        final TreeTableColumn<Entry, String> categoryColumn = new TreeTableColumn<>("Category");
        categoryColumn.setPrefWidth(UIConstants.CATEGORY_WIDTH);
        categoryColumn.setCellValueFactory(param -> param.getValue().getValue().categoryProperty());

        final TreeTableColumn<Entry, String> commentColumn = new TreeTableColumn<>("Comment");
        commentColumn.setPrefWidth(UIConstants.COMMENT_WIDTH);
        commentColumn.setCellValueFactory(param -> param.getValue().getValue().commentProperty());

        entryTable.getColumns().clear();
        entryTable.getColumns().add(dateColumn);
        entryTable.getColumns().add(sequenceColumn);
        entryTable.getColumns().add(amountColumn);
        entryTable.getColumns().add(savingColumn);
        entryTable.getColumns().add(categoryColumn);
        entryTable.getColumns().add(commentColumn);
    }

    @FXML
    public void filter() {

        fillTable(entries.stream().filter(this::filter).collect(Collectors.toList()));

    }

    @FXML
    public void handleEscape(KeyEvent keyEvent) {

        KeyCode code = keyEvent.getCode();
        if (code == KeyCode.ESCAPE) {
            clearFilter();
        }
    }

    @FXML
    public void clearFilter() {

        this.filter.setText("");
        this.filter();
    }

    private boolean filter(final Entry entry) {

        final String filter = this.filter.getText();

        if (filter == null || filter.isEmpty()) {
            return true;
        } else {

            return StringUtils.containsIgnoreCase(entry.getComment(), filter) || StringUtils.containsIgnoreCase(entry.getCategory(), filter);

        }

    }

}
