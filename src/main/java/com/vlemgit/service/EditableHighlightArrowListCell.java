package com.vlemgit.service;

import com.vlemgit.model.ConfigurationModel;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.input.KeyCode;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.util.StringConverter;
import javafx.scene.control.Alert.AlertType;


public class EditableHighlightArrowListCell extends TextFieldListCell<String> {

    private final ConfigurationManager configurationManager;
    private final ConfigurationModel configurationModel;
    public EditableHighlightArrowListCell(ConfigurationModel configurationModel, ConfigurationManager configurationManager, StringConverter<String> converter) {
        super(converter);
        this.configurationManager = configurationManager;
        this.configurationModel = configurationModel;
    }

    @Override
    public void startEdit() {
        super.startEdit();
        if (isEditing()) {
            TextField textField = new TextField(getItem());
            textField.setOnAction(event -> commitEdit(textField.getText()));
            setGraphic(textField);
            setText(null);
            textField.requestFocus();
        }
    }

    @Override
    public void commitEdit(String newValue) {
        if (configurationManager.isValidUrlAndSurroundedWellByArrows(newValue, getItem())) {
            String oldValue = getItem();
            super.commitEdit(newValue);
            configurationManager.setSetting("UrlLinks", oldValue, newValue);
            configurationManager.saveConfig();
        } else {
            new Alert(AlertType.ERROR, "Invalid URL", "not gut");
            updateItem(getItem(), false);
        }
    }

    @Override
    public void cancelEdit() {
        super.cancelEdit();
        updateItem(getItem(), false);
    }

    @Override
    public void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) {
            setText(null);
            setGraphic(null);
        } else {
            if (isEditing()) {
                setText(item);
                setGraphic(null);
            } else {
                String[] parts = item.split("->");
                TextFlow textFlow = new TextFlow();
                for (int i = 0; i < parts.length; i++) {
                    textFlow.getChildren().add(new Text(parts[i]));
                    if (i < parts.length - 1) {
                        Text arrow = new Text("->");
                        arrow.setStyle("-fx-fill: red; -fx-font-weight: bold;");
                        textFlow.getChildren().add(arrow);
                    }
                }
                setGraphic(textFlow);
                setText(null);
            }
        }
    }
}