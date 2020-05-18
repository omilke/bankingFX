package de.omilke.bankingfx.main.importer;

import de.saxsys.mvvmfx.FxmlView;

public class ImporterView implements FxmlView<ImporterModel> {

//    private EntryRepository es = PersistenceProvider.getEntryRepository();
//
//    private CategoryRepository cs = PersistenceProvider.getCategoryRepository();
//
//    private Locale locale = BankingConfigurator.provideLocale();
//
//    @FXML
//    TableView<Entry> entryTable;
//
//    @FXML
//    Button importButton;
//
//    @FXML
//    TextArea importTextArea;
//
//    @FXML
//    Button saveButton;
//
//    @FXML
//    Accordion accordion;
//
//    private Runnable refreshAction;
//
//    public void initialize() {
//
//        this.accordion.setExpandedPane(this.accordion.getPanes().get(0));
//
//        entryTable.setColumnResizePolicy((param) -> true);
//        entryTable.setEditable(true);
//
//        final List<String> entries = this.cs.findAllCategories().stream().map(Category::getName).collect(Collectors.toList());
//        setColumns(entries);
//
//        Platform.runLater(() -> importTextArea.requestFocus());
//    }
//
//    private void setColumns(final List<String> categories) {
//
//        final TableColumn<Entry, LocalDate> dateColumn = new TableColumn<>("Date");
//        dateColumn.setPrefWidth(UIConstants.MONTH_WIDTH);
//        dateColumn.getStyleClass().add(UIConstants.ALIGN_CENTER);
//        dateColumn.setCellValueFactory(param -> param.getValue().entryDateProperty());
//        dateColumn.setCellFactory(param -> new DateEditingCell<>(UIConstants.DATE_FORMATTER));
//        dateColumn.setOnEditCommit(e -> {
//            e.getTableView().getItems().get(e.getTablePosition().getRow()).setEntryDate(e.getNewValue());
//        });
//
//        final TableColumn<Entry, EntryOrder> sequenceColumn = new TableColumn<>("");
//        sequenceColumn.setPrefWidth(UIConstants.SEQUENCE_WIDTH);
//        sequenceColumn.getStyleClass().add(UIConstants.ALIGN_CENTER);
//        sequenceColumn.setCellValueFactory(param -> param.getValue().entryOrderProperty());
//        sequenceColumn.setCellFactory(param -> new TableCell<Entry, EntryOrder>() {
//
//            @Override
//            protected void updateItem(final EntryOrder item, final boolean empty) {
//
//                super.updateItem(item, empty);
//
//                final Entry entry = (Entry) getTableRow().getItem();
//
//                if (item == null || empty || entry == null) {
//                    setGraphic(null);
//                } else {
//                    final Text icon;
//
//                    switch (item.getSequence()) {
//                        case FIRST:
//                            icon = FontAwesomeIconFactory.get().createIcon(FontAwesomeIcon.CHEVRON_CIRCLE_UP);
//                            break;
//                        case LAST:
//                            icon = FontAwesomeIconFactory.get().createIcon(FontAwesomeIcon.CHEVRON_CIRCLE_DOWN);
//                            break;
//                        default:
//                            icon = null;
//                            break;
//                    }
//
//                    setGraphic(icon);
//
//                    final EntryOrder entryOrder = entry.getEntryOrder();
//
//                    final ContextMenu addMenu = createContextMenu(e -> {
//
//                        final SequenceeditorModel edit = new SequenceeditorModel();
////                        edit.openDialog(entryOrder.getSequence(), entryOrder.getOrderIndex(), new UpdateEntry(entry));
//                    });
//
//                    setContextMenu(addMenu);
//                }
//            }
//
//        });
//
//        final TableColumn<Entry, BigDecimal> amountColumn = new TableColumn<>("Amount");
//        amountColumn.setPrefWidth(UIConstants.AMOUNT_WIDTH);
//        amountColumn.getStyleClass().add(UIConstants.ALIGN_RIGHT);
//        amountColumn.setCellValueFactory(param -> param.getValue().amountProperty());
//        amountColumn.setCellFactory(column -> new AmountEditingCell<>(locale));
//        amountColumn.setOnEditCommit(e -> {
//            e.getTableView().getItems().get(e.getTablePosition().getRow()).setAmount(e.getNewValue());
//        });
//
//        final TableColumn<Entry, Boolean> savingColumn = new TableColumn<>("Saving");
//        savingColumn.setPrefWidth(UIConstants.SAVING_WIDTH);
//        savingColumn.getStyleClass().add(UIConstants.ALIGN_CENTER);
//        savingColumn.setCellValueFactory(param -> param.getValue().savingProperty());
//        savingColumn.setCellFactory(param -> new SavingEditingCell<>());
//        savingColumn.setOnEditCommit(e -> {
//            e.getTableView().getItems().get(e.getTablePosition().getRow()).setSaving(e.getNewValue());
//        });
//
//        final TableColumn<Entry, String> categoryColumn = new TableColumn<>("Category");
//        categoryColumn.setPrefWidth(UIConstants.CATEGORY_WIDTH);
//        categoryColumn.getStyleClass().add(UIConstants.ALIGN_LEFT);
//        categoryColumn.setCellValueFactory(param -> param.getValue().categoryProperty());
//        categoryColumn.setCellFactory(ComboBoxTableCell.forTableColumn(FXCollections.observableList(categories)));
//        categoryColumn.setOnEditCommit(e -> {
//            e.getTableView().getItems().get(e.getTablePosition().getRow()).setCategory(e.getNewValue());
//        });
//
//        final TableColumn<Entry, String> commentColumn = new TableColumn<>("Comment");
//        commentColumn.setPrefWidth(UIConstants.COMMENT_IMPORT_WIDTH);
//        commentColumn.getStyleClass().add(UIConstants.ALIGN_LEFT);
//        commentColumn.setCellValueFactory(param -> param.getValue().commentProperty());
//        commentColumn.setCellFactory(TextFieldTableCell.forTableColumn());
//        commentColumn.setOnEditCommit(e -> {
//            e.getTableView().getItems().get(e.getTablePosition().getRow()).setComment(e.getNewValue());
//        });
//
//        TableColumn<Entry, Boolean> deleteColumn = new TableColumn<>("");
//        deleteColumn.setPrefWidth(UIConstants.ACTION_IMPORT_WIDTH);
//        deleteColumn.getStyleClass().add(UIConstants.ALIGN_CENTER);
//        deleteColumn.setSortable(false);
//
//        // define a simple boolean cell value for the action column so that the column will only be shown for non-empty rows.
//        deleteColumn.setCellValueFactory(features -> new SimpleBooleanProperty(features.getValue() != null));
//
//        // getEntryRepository a cell value factory with an add button for each row in the table.
//        deleteColumn.setCellFactory(personBooleanTableColumn -> new DeleteButtonCell());
//
//
//        entryTable.getColumns().addAll(dateColumn, sequenceColumn, amountColumn, savingColumn, categoryColumn, commentColumn, deleteColumn);
//
//    }
//
//    private ContextMenu createContextMenu(final EventHandler<ActionEvent> eventHandler) {
//
//        final MenuItem menuItem = new MenuItem("Set Entry Order");
//        menuItem.setOnAction(eventHandler);
//
//        final ContextMenu menu = new ContextMenu();
//        menu.getItems().add(menuItem);
//
//        return menu;
//    }
//
//    @FXML
//    public void saveEntries() {
//
//        final List<Category> findAllCategories = this.cs.findAllCategories();
//
//        final Map<String, Category> categoryMap = new HashMap<>();
//        for (final Category category : findAllCategories) {
//            categoryMap.put(category.getName(), category);
//        }
//
//        final ObservableList<Entry> items = this.entryTable.getItems();
//
//        for (final Entry entry : items) {
//
//            final Category category = getCategory(categoryMap, entry.getCategory());
//
//            final de.omilke.banking.account.entity.Entry entity = new de.omilke.banking.account.entity.Entry(
//                    entry.getEntryDate(),
//                    entry.getAmount(),
//                    entry.getSaving(),
//                    entry.getEntryOrder().getSequence(),
//                    entry.getEntryOrder().getOrderIndex(),
//                    entry.getComment(),
//                    category);
//
//            this.es.update(entity);
//        }
//
//        this.refreshAction.run();
//
//        Stage stage = (Stage) this.entryTable.getScene().getWindow();
//        stage.close();
//    }
//
//    private Category getCategory(final Map<String, Category> categoryMap, final String categoryToFind) {
//
//        final Category result;
//
//        final Category lookedUpCategory = categoryMap.get(categoryToFind);
//        if (lookedUpCategory == null) {
//            result = new Category(categoryToFind);
//        } else {
//            result = lookedUpCategory;
//
//        }
//
//        return result;
//    }
//
//    @FXML
//    public void performImport() {
//
//        final IngDibaStatementConverter converter = IngDibaStatementConverter.prepare();
//
//        final List<ParserResult> convert = converter.convert(this.importTextArea.getText());
//
//        this.fillTable(convert);
//
//    }
//
//    private void fillTable(final List<ParserResult> convert) {
//
//        final List<Entry> collect = convert
//                .stream()
//                .map(
//                        current -> new Entry(
//                                current.getEntry().getEntryDate(),
//                                current.getEntry().getSequence(),
//                                current.getEntry().getOrderIndex(),
//                                current.getEntry().getAmount(),
//                                current.getEntry().isSaving(),
//                                current.getCategoryName(),
//                                current.getEntry().getComment()))
//                .collect(Collectors.toList());
//
//        this.entryTable.getItems().clear();
//        this.entryTable.getItems().addAll(collect);
//
//        this.accordion.setExpandedPane(this.accordion.getPanes().get(1));
//    }
//
//    public void setRefreshAction(Runnable refreshAction) {
//
//        this.refreshAction = refreshAction;
//    }
//
///**
//     * Displays a Delete button, that removes the line in which the button was activated.
//     *
//     * @author Oliver Milke
//     * @since 11.12.2015
//
//*/
//    private class DeleteButtonCell extends TableCell<Entry, Boolean> {
//
//        // a button for adding a new person.
//        final Button addButton = new Button();
//
//        // pads and centers the add button in the cell.
//        final StackPane paddedButton = new StackPane();
//
//        DeleteButtonCell() {
//
//            FontAwesomeIconFactory.get().setIcon(addButton, FontAwesomeIcon.TRASH, "1.25em");
//
//            paddedButton.setPadding(new Insets(3));
//            paddedButton.getChildren().add(addButton);
//
//            addButton.setOnAction(actionEvent -> {
//
//                //remove selected item from the table list
//                getTableView().getItems().remove(getIndex());
//            });
//        }
//
//        @Override
//        protected void updateItem(Boolean item, boolean empty) {
//
//            super.updateItem(item, empty);
//
//            if (!empty) {
//                //places the button in the row only if the row is not empty.
//                setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
//                setGraphic(paddedButton);
//            } else {
//                setGraphic(null);
//            }
//        }
//    }
//
//
///**
//     * Consumer that will update the provided {@link Entry} once it is invoked.
//     *
//     * @author Oliver Milke
//     * @since 24.08.2015
//
//*/
//    private static final class UpdateEntry implements Consumer<EntryOrderSetting> {
//
//        private final Entry entry;
//
///**
//         * @param entry The {@link Entry} that shall be updated.
//  */
//
//        private UpdateEntry(final Entry entry) {
//
//            this.entry = entry;
//        }
//
//        @Override
//        public void accept(final EntryOrderSetting consumed) {
//
//            final EntryOrder entryOrder = entry.getEntryOrder();
//            entry.setEntryOrder(entryOrder.update(consumed.getEntrySequence(), consumed.getOrderIndex()));
//        }
//    }

}
