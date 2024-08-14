package com.vlemgit.controller;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import org.apache.commons.validator.routines.UrlValidator;

import com.vlemgit.service.ConfigurationManager;
import com.vlemgit.service.DirectoryChooserUtil;
import com.vlemgit.service.EditableHighlightArrowListCell;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Window;
import javafx.util.converter.DefaultStringConverter;

public class ConfiguratorController {

    @FXML
    private TextField directoryPathField;

    @FXML
    private ListView<String> listView;

    @FXML
    private TextField newItemField;

    private ObservableList<String> items;

    private ConfigurationManager configurationManager;

    @FXML
    public void initialize() {
        configurationManager = new ConfigurationManager("src\\main\\resources\\com\\vlemgit\\settings.conf");

        this.items = FXCollections.observableArrayList("http://qa.efact-api.test.paas/->http://localhost:8086/",
                "http://localhost:9095/->https://qa.tarifrulesexecuter.paas/",
                "http://qa.efact-ui.test.paas/->http://localhost:8087/->http://efact-ui.acc.paas/");
        listView.setItems(items);
        listView.setCellFactory(param -> new EditableHighlightArrowListCell(new DefaultStringConverter()));
        listView.setPrefHeight(250);
        final String[] oldValue = new String[1];

        listView.setOnEditStart(event -> {
            oldValue[0] = listView.getItems().get(event.getIndex());
        });

        listView.setOnEditCommit(event -> {
            String newRowValue = event.getNewValue(); // http://qa.efact-ui.test.paas/->http://localhost:8087/->http://efact-ui.acc.paas/->http://efact-ui.acc.paas/
            String previousValue = oldValue[0];       // http://qa.efact-ui.test.paas/->http://localhost:8087/->http://efact-ui.acc.paas/
            String newValue = newRowValue.replace(previousValue, ""); //  ->http://efact-ui.acc.paas/


            if (isValidUrlAndSurroundedWellByArrows(newRowValue, newValue)) {
                listView.getItems().set(event.getIndex(), newValue);
            } else {
                showAlert("Invalid format", "The input must be in the format 'url1->url2->url3'.");
                listView.refresh();
            }
        });

        loadSettings();
    }

    public boolean isValidUrlAndSurroundedWellByArrows(String newRowValue, String newValue) {
        String groupSeparator = "->";


    String[] groups = newRowValue.split(Pattern.quote(groupSeparator));


    for (String group : groups) {
        if (!isValidUrl(group.trim())) {
            return false;
        }
    }


    if (newValue.startsWith(groupSeparator) || newValue.endsWith(groupSeparator)) {
        return false;
    }


    if (!newValue.isBlank()) {
        String combinedValue = newRowValue + newValue;
        String[] combinedGroups = combinedValue.split(Pattern.quote(groupSeparator));

        for (String group : combinedGroups) {
            if (group.isBlank() || !isValidUrl(group.trim())) {
                return false;
            }
        }
    }

    return true;

    }

    private String findChangedPart(String oldValue, String newValue) {

        int minLength = Math.min(oldValue.length(), newValue.length());
        int index = 0;

        while (index < minLength && oldValue.charAt(index) == newValue.charAt(index)) {
            index++;
        }


        if (index == oldValue.length()) {
            return newValue.substring(index);
        } else if (index == newValue.length()) {
            return "";
        } else {
            return newValue.substring(index);
        }
    }

    @FXML
    private void openDirectoryChooser() {
        Window window = directoryPathField.getScene().getWindow();
        File selectedDirectory = DirectoryChooserUtil.chooseDirectory(window);

        if (selectedDirectory != null) {
            directoryPathField.setText(selectedDirectory.getAbsolutePath());
        }
    }

    @FXML
    private void addItem() {
        String newItem = newItemField.getText();
        if (!items.isEmpty()) {
            newItem = items.get(items.size() - 1) + "->" + newItem;
        }

        if (isValidUrl(newItem)) {
            items.add(newItem);
            newItemField.clear();
        } else {
            showAlert("Invalid format", "The input must be in the format 'url1->url2->url3'.");
        }
    }

    @FXML
    private void saveSettings() {
        configurationManager.setSetting("directoryPath", directoryPathField.getText());
        // Sauvegardez d'autres paramètres si nécessaire
        configurationManager.saveConfig();
    }

    private boolean isValidUrl(String input) {
        /* //String urlPattern = "https?:\\/\\/[a-zA-Z0-9\\-]+(\\.[a-zA-Z0-9\\-]+)+(:\\d+)?\\(\\/\\[a-zA-Z0-9\\-._~:\\/?#\\[\\]@!$&'()*\\+,;=]*\\)?";
        //String urlPattern = "https?:\\/\\/(?:www\\.|(?!www))[a-zA-Z0-9][a-zA-Z0-9-]+[a-zA-Z0-9]\\.[^\\s]{2,}|www\\.[a-zA-Z0-9][a-zA-Z0-9-]+[a-zA-Z0-9]\\.[^\\s]{2,}|https?:\\/\\/(?:www\\.|(?!www))[a-zA-Z0-9]+\\.[^\\s]{2,}|www\\.[a-zA-Z0-9]+\\.[^\\s]{2,}";
        String urlPattern = "((http|https)://)(www.)?[a-zA-Z0-9@:%._\\\\+~#?&//=]{2,256}\\\\.[a-z]{2,6}\\\\b([-a-zA-Z0-9@:%._\\\\+~#?&//=]*)";
        String regex = "^\\(?:" + urlPattern + "\\(->" + urlPattern + "\\)*\\)$";
        System.out.println(regex);

        return input.matches(regex); */
        String[] schemes = {"http","https"};
        UrlValidator validator = new UrlValidator(schemes , UrlValidator.ALLOW_LOCAL_URLS);
        return validator.isValid(input);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void loadSettings() {
        directoryPathField.setText(configurationManager.getSetting("directoryPath"));
    }
}
