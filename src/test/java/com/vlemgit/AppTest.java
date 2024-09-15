package com.vlemgit;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import static org.testfx.assertions.api.Assertions.*;

class AppTest extends ApplicationTest {

    private Label label;
    @Override
    public void start(Stage stage) {
        label = new Label("No file loaded");
        Button button = new Button("Load File");
        button.setOnAction(e -> label.setText("File loaded"));

        StackPane root = new StackPane(button, label);
        stage.setScene(new javafx.scene.Scene(root, 300, 200));
        stage.show();
    }

/*    @Test
    void should_update_label_when_button_clicked() {
        // Click on the button
        clickOn("Load File");

        // Verify that the label text has changed
        assertThat(label.getText()).isEqualTo("File loaded");
    }*/
}
