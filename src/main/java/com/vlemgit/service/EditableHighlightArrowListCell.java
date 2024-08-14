package com.vlemgit.service;

import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.util.StringConverter;


public class EditableHighlightArrowListCell extends TextFieldListCell<String> {

    public EditableHighlightArrowListCell(StringConverter<String> converter) {
        super(converter);
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
                // Split the item into parts around "->"
                String[] parts = item.split("->");

                // Create a TextFlow to hold the parts
                TextFlow textFlow = new TextFlow();

                for (int i = 0; i < parts.length; i++) {
                    // Add normal text
                    textFlow.getChildren().add(new Text(parts[i]));

                    // Add highlighted arrow if it's not the last part
                    if (i < parts.length - 1) {
                        Text arrow = new Text("->");
                        arrow.setStyle("-fx-fill: red; -fx-font-weight: bold;"); // Highlight style
                        textFlow.getChildren().add(arrow);
                    }
                }

                setGraphic(textFlow);
                setText(null);
            }
        }
    }
}