package de.omilke.bankingfx.main.converter;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import de.omilke.banking.BankingConfigurator;
import de.omilke.banking.account.EntryService;
import de.omilke.banking.interop.exporting.LinesWriter;
import de.omilke.banking.interop.exporting.jhaushalt.JHaushaltFormatter;
import de.omilke.banking.interop.importing.parser.ingdiba.IngDibaCsvExportConverter;
import de.omilke.bankingfx.UIConstants;
import de.omilke.bankingfx.controls.*;
import de.omilke.bankingfx.main.entrylist.model.Entry;
import de.omilke.bankingfx.main.entrylist.model.EntryOrder;
import de.omilke.bankingfx.main.sequenceeditor.SequenceeditorView;
import de.omilke.bankingfx.main.sequenceeditor.model.EntryOrderSetting;
import de.saxsys.mvvmfx.FxmlView;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

public class ConverterView implements FxmlView<ConverterModel> {

    private EntryService es = EntryService.INSTANCE;

    private Locale locale = BankingConfigurator.INSTANCE.configuredLocale();

    @FXML
    CheckBox postponeCheckBox;

    @FXML
    Label entryCounterLabel;

    @FXML
    TableView<Entry> entryTable;

    public void initialize() {

        final List<String> categories = this.es.getAllCategories();
        this.setColumns(categories);

        this.entryTable.getItems().addListener(this::onItemsChanged);
    }

    @FXML
    public void actionSaveToFile() {

        DefaultFileChooser fileChooser = new DefaultFileChooser("converted.csv", entryTable.getScene().getWindow(), "Select Save File", new FileChooser.ExtensionFilter("CSV files", "*.csv"));

        saveToSelectedFile(fileChooser.showSave());
    }

    private void saveToSelectedFile(Optional<File> result) {

        if (result.isPresent()) {

            LinesWriter.INSTANCE.saveLinesToFile(result.get(), formatEntries(entryTable.getItems()));
        }
    }

    private List<String> formatEntries(List<Entry> entriesToExport) {

        //TODO: supply dictionary for comment -> category to ease import in JHaushalt
        JHaushaltFormatter formatter = new JHaushaltFormatter(false);

        List<String> result = new ArrayList<>();
        for (Entry currentEntry : entriesToExport) {

            final LocalDate entryDate;
            if (postponeCheckBox.isSelected()) {
                entryDate = currentEntry.getEntryDate().plusYears(1);
            } else {
                entryDate = currentEntry.getEntryDate();
            }

            result.add(formatter.format(entryDate, currentEntry.getAmount(), currentEntry.getComment(), currentEntry.getCategory()));
        }

        return result;
    }

    @FXML
    public void actionOpenFileForConversion() {

        DefaultFileChooser fileChooser = new DefaultFileChooser("", entryTable.getScene().getWindow(), "Select File For Conversion", new FileChooser.ExtensionFilter("all file types (*.*)", "*.*"));
        convertSelectedFile(fileChooser.showOpen());
    }

    private void convertSelectedFile(Optional<File> selectedFile) {

        if (selectedFile.isPresent()) {
            Path path = selectedFile.get().toPath();

            try {
                List<String> lines = Files.readAllLines(path, StandardCharsets.ISO_8859_1);

                this.performConversion(lines);
            } catch (IOException e) {
                Logger logger = LogManager.getLogger(this.getClass());
                logger.log(Level.WARN, "File could not be read with ANSI encoding: {}", path.toString(), e);
            }
        }
    }

    private void performConversion(List<String> lines) {

        List<de.omilke.banking.account.entity.Entry> convertedEntries = new IngDibaCsvExportConverter().convert(lines);
        fillTable(convertedEntries);
    }

    private void fillTable(final List<de.omilke.banking.account.entity.Entry> parsedEntries) {

        final List<Entry> entries = parsedEntries
                .stream()
                .map(Entry::new)
                .collect(Collectors.toList());

        this.entryTable.getItems().clear();
        this.entryTable.getItems().addAll(entries);
    }

    private void setColumns(final List<String> categories) {

        final TableColumn<Entry, LocalDate> dateColumn = new TableColumn<>("Date");
        dateColumn.setPrefWidth(UIConstants.MONTH_WIDTH);
        dateColumn.getStyleClass().add(UIConstants.ALIGN_CENTER);
        dateColumn.setCellValueFactory(param -> param.getValue().entryDateProperty());
        dateColumn.setCellFactory(param -> new DateEditingCell<>(UIConstants.DATE_FORMATTER));
        dateColumn.setOnEditCommit(e -> e.getTableView().getItems().get(e.getTablePosition().getRow()).setEntryDate(e.getNewValue()));

        final TableColumn<Entry, EntryOrder> sequenceColumn = new TableColumn<>("");
        sequenceColumn.setPrefWidth(UIConstants.SEQUENCE_WIDTH);
        sequenceColumn.getStyleClass().add(UIConstants.ALIGN_CENTER);
        sequenceColumn.setCellValueFactory(param -> param.getValue().entryOrderProperty());
        sequenceColumn.setCellFactory(param -> new TableCell<>() {

            @Override
            protected void updateItem(final EntryOrder item, final boolean empty) {

                super.updateItem(item, empty);

                final Entry entry = getTableRow().getItem();

                if (item == null || empty || entry == null) {
                    setGraphic(null);
                } else {
                    final Text icon;

                    switch (item.getSequence()) {
                        case FIRST:
                            icon = UIUtils.getIcon(FontAwesomeIcon.CHEVRON_CIRCLE_UP);
                            break;
                        case LAST:
                            icon = UIUtils.getIcon(FontAwesomeIcon.CHEVRON_CIRCLE_DOWN);
                            break;
                        default:
                            icon = null;
                            break;
                    }

                    setGraphic(icon);

                    final EntryOrder entryOrder = entry.getEntryOrder();

                    final ContextMenu addMenu = createContextMenu(e -> {

                        Optional<EntryOrderSetting> entryOrderSetting = SequenceeditorView.openView(entryOrder.getSequence(), entryOrder.getOrderIndex());
                        entryOrderSetting.ifPresent(p -> entry.setEntryOrder(entryOrder.update(p.getEntrySequence(), p.getOrderIndex())));
                    });

                    setContextMenu(addMenu);
                }
            }
        });

        final TableColumn<Entry, BigDecimal> amountColumn = new TableColumn<>("Amount");
        amountColumn.setPrefWidth(UIConstants.AMOUNT_WIDTH);
        amountColumn.getStyleClass().add(UIConstants.ALIGN_RIGHT);
        amountColumn.setCellValueFactory(param -> param.getValue().amountProperty());
        amountColumn.setCellFactory(column -> new AmountEditingCell<>(locale));
        amountColumn.setOnEditCommit(e -> e.getTableView().getItems().get(e.getTablePosition().getRow()).setAmount(e.getNewValue()));

        final TableColumn<Entry, Boolean> savingColumn = new TableColumn<>("Saving");
        savingColumn.setPrefWidth(UIConstants.SAVING_WIDTH);
        savingColumn.getStyleClass().add(UIConstants.ALIGN_CENTER);
        savingColumn.setCellValueFactory(param -> param.getValue().savingProperty());
        savingColumn.setCellFactory(param -> new SavingEditingCell<>());
        savingColumn.setOnEditCommit(e -> e.getTableView().getItems().get(e.getTablePosition().getRow()).setSaving(e.getNewValue()));

        final TableColumn<Entry, String> categoryColumn = new TableColumn<>("Category");
        categoryColumn.setPrefWidth(UIConstants.CATEGORY_WIDTH);
        categoryColumn.getStyleClass().add(UIConstants.ALIGN_LEFT);
        categoryColumn.setCellValueFactory(param -> param.getValue().categoryProperty());
        categoryColumn.setCellFactory(ComboBoxTableCell.forTableColumn(FXCollections.observableList(categories)));
        categoryColumn.setOnEditCommit(e -> e.getTableView().getItems().get(e.getTablePosition().getRow()).setCategory(e.getNewValue()));

        final TableColumn<Entry, String> commentColumn = new TableColumn<>("Comment");
        commentColumn.setPrefWidth(UIConstants.COMMENT_IMPORT_WIDTH);
        commentColumn.getStyleClass().add(UIConstants.ALIGN_LEFT);
        commentColumn.setCellValueFactory(param -> param.getValue().commentProperty());
        commentColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        commentColumn.setOnEditCommit(e -> e.getTableView().getItems().get(e.getTablePosition().getRow()).setComment(e.getNewValue()));

        TableColumn<Entry, Boolean> deleteColumn = new TableColumn<>("");
        deleteColumn.setPrefWidth(UIConstants.ACTION_IMPORT_WIDTH);
        deleteColumn.getStyleClass().add(UIConstants.ALIGN_CENTER);
        deleteColumn.setSortable(false);

        // define a simple boolean cell value for the action column so that the column will only be shown for non-empty rows.
        deleteColumn.setCellValueFactory(features -> new SimpleBooleanProperty(features.getValue() != null));

        // getEntryRepository a cell value factory with an add button for each row in the table.
        deleteColumn.setCellFactory(personBooleanTableColumn -> new ButtonBarCell());

        entryTable.getColumns().clear();
        entryTable.getColumns().add(dateColumn);
        entryTable.getColumns().add(sequenceColumn);
        entryTable.getColumns().add(amountColumn);
        entryTable.getColumns().add(savingColumn);
        entryTable.getColumns().add(categoryColumn);
        entryTable.getColumns().add(commentColumn);
        entryTable.getColumns().add(deleteColumn);
    }

    private void onItemsChanged(ListChangeListener.Change<? extends Entry> change) {

        ObservableList<? extends Entry> observableValue = change.getList();

        if (observableValue == null || observableValue.isEmpty()) {
            entryCounterLabel.setText("0");
        } else {
            entryCounterLabel.setText(observableValue.size() + "");
        }
    }

    private ContextMenu createContextMenu(final EventHandler<ActionEvent> eventHandler) {

        final MenuItem menuItem = new MenuItem("Set Entry Order");
        menuItem.setOnAction(eventHandler);

        final ContextMenu menu = new ContextMenu();
        menu.getItems().add(menuItem);

        return menu;
    }

    /**
     * Displays a Delete button, that removes the line in which the button was activated.
     */
    private class ButtonBarCell extends TableCell<Entry, Boolean> {

        HBox buttonBar = new HBox();

        // a button for adding a new person.


        ButtonBarCell() {

            // pads and centers the add button in the cell.
            buttonBar.setAlignment(Pos.BASELINE_CENTER);
            //buttonBar.setSpacing(1.0);


            buttonBar.getChildren().add(buildButton(FontAwesomeIcon.TRASH, this::deleteButtonPressed));
            buttonBar.getChildren().add(buildButton(FontAwesomeIcon.COPY, this::splitButtonPressed));
        }

        private Node buildButton(FontAwesomeIcon icon, EventHandler<ActionEvent> onAction) {

            final Button button = new Button();
            button.setGraphic(new FontAwesomeIconView(icon, "1.25em"));
            button.setContentDisplay(ContentDisplay.CENTER);
            button.setOnAction(onAction);


            final StackPane paddedButton = new StackPane(button);
            paddedButton.setPadding(new Insets(3));

            return paddedButton;
        }

        @Override
        protected void updateItem(Boolean item, boolean empty) {

            super.updateItem(item, empty);

            if (!empty) {
                //places the button in the row only if the row is not empty.
                setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                setGraphic(buttonBar);
            } else {
                setGraphic(null);
            }
        }

        private void deleteButtonPressed(ActionEvent actionEvent) {

            getTableView().getItems().remove(getIndex());
        }

        private void splitButtonPressed(ActionEvent actionEvent) {

            new SplitEntryPopover(getTableView().getItems(), getTableRow().getItem()).show((Node) actionEvent.getSource());
        }


    }

}
