package de.omilke.bankingfx.controls;

//import javafx.collections.FXCollections;
//import javafx.scene.control.ComboBox;
//import javafx.scene.control.ListCell;
//import javafx.scene.control.TableCell;
//
//class SequenceEditingCell<P> extends TableCell<P, String> {
//
//    private ComboBox<String> comboBox;
//
//    public SequenceEditingCell() {
//
//    }
//
//    @Override
//    public void startEdit() {
//
//        if (!isEmpty()) {
//            super.startEdit();
//            createComboBox();
//            setText(null);
//            setGraphic(comboBox);
//        }
//    }
//
//    @Override
//    public void cancelEdit() {
//
//        super.cancelEdit();
//
//        setText(getItem());
//        setGraphic(null);
//    }
//
//    @Override
//    public void updateItem(final String item, final boolean empty) {
//
//        super.updateItem(item, empty);
//
//        if (empty) {
//            setText(null);
//            setGraphic(null);
//        } else {
//            if (isEditing()) {
//                if (comboBox != null) {
//                    comboBox.setValue(getItem());
//                }
//                setText(getItem());
//                setGraphic(comboBox);
//            } else {
//                setText(getItem());
//                setGraphic(null);
//            }
//        }
//    }
//
//    private void createComboBox() {
//
//        comboBox = new ComboBox<>(FXCollections.observableArrayList("Auto", "Bargeld", "Einkaufen"));
//        comboBoxConverter(comboBox);
//        comboBox.valueProperty().set(getItem());
//        comboBox.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);
//        comboBox.setOnAction((e) -> {
//            System.out.println("Committed: " + comboBox.getSelectionModel().getSelectedItem());
//            commitEdit(comboBox.getSelectionModel().getSelectedItem());
//        });
//// comboBox.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
//// if (!newValue) {
//// commitEdit(comboBox.getSelectionModel().getSelectedItem());
//// }
//// });
//    }
//
//    private void comboBoxConverter(final ComboBox<String> comboBox) {
//        // Define rendering of the list of values in ComboBox drop down.
//        comboBox.setCellFactory((c) -> new ListCell<String>() {
//
//            @Override
//            protected void updateItem(final String item, final boolean empty) {
//
//                super.updateItem(item, empty);
//
//                if (item == null || empty) {
//                    setText(null);
//                } else {
//                    setText(item);
//                }
//            }
//        });
//    }
//}