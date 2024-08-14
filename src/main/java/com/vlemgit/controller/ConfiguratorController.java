package com.vlemgit.controller;

import java.io.File;
import java.util.regex.Pattern;


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
    private ListView<String> listViewUrlLinks;

    @FXML
    private TextField newUrlField;


    @FXML
    private ListView<String> listViewPerformance;
    
    @FXML
    private TextField newPerfFolderField;
    private ObservableList<String> observableUrlItems;

    private ConfigurationManager configurationManager;

    @FXML
    public void initialize() {
        configurationManager = new ConfigurationManager("src\\main\\resources\\com\\vlemgit\\settings.conf");

        this.observableUrlItems = FXCollections.observableArrayList();
    /*observableArrayList("http://qa.efact-api.test.paas/->http://localhost:8086/",
                "http://localhost:9095/->https://qa.tarifrulesexecuter.paas/",
                "http://qa.efact-ui.test.paas/->http://localhost:8087/->http://efact-ui.acc.paas/");*/
        listViewUrlLinks.setItems(observableUrlItems);
        listViewUrlLinks.setCellFactory(param -> new EditableHighlightArrowListCell(new DefaultStringConverter()));
        listViewUrlLinks.setPrefHeight(250);
        final String[] oldValue = new String[1];
        newUrlField.setOnAction(event -> addUrls());

        listViewUrlLinks.setOnEditStart(event -> oldValue[0] = listViewUrlLinks.getItems().get(event.getIndex()));

        listViewUrlLinks.setOnEditCommit(event -> {
            String newRowValue = event.getNewValue();
            String previousValue = oldValue[0];


            if (isValidUrlAndSurroundedWellByArrows(newRowValue, previousValue)) {
                listViewUrlLinks.getItems().set(event.getIndex(), newRowValue);
                if(newRowValue.isEmpty()){
                    listViewUrlLinks.getItems().remove(event.getIndex());
                    listViewUrlLinks.getSelectionModel().clearSelection();
                }
            } else {
                showAlert("Invalid format", "The input must be in the format : \n url1 \n url1->url2 \n url1->url2->url3->urlX... ");
                listViewUrlLinks.refresh();
            }
        });
        
        
        
        
        
        
        loadSettings();
    }

    public boolean isValidUrlAndSurroundedWellByArrows(String newRowValue, String previousValue) {

        String groupSeparator = "->";
        String[] newRowgroups = newRowValue.split(Pattern.quote(groupSeparator));
        String[] previousValuegroups = previousValue.split(Pattern.quote(groupSeparator));

        for (String group : newRowgroups) {
            if (!group.trim().isEmpty() && !isValidUrl(group.trim())) {
                return false;
            }
        }

        for (String group : previousValuegroups) {
            if (!group.trim().isEmpty() && !isValidUrl(group.trim())) {
                return false;
            }
        }
        return true;
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
    private void addUrls() {
        String newItem = newUrlField.getText();
        if (isValidUrl(newItem)) {
            observableUrlItems.add(newItem);
            newUrlField.clear();
        } else {
            showAlert("Invalid format", "The input must be in the format : \n url1 \n url1->url2 \n url1->url2->url3->urlX... ");
        }
    }

    @FXML
    private void addPerformanceFolder(){

    }

    @FXML
    private void saveSettings() {
        configurationManager.setSetting("directoryPath", directoryPathField.getText());
        // Sauvegardez d'autres paramètres si nécessaire
        configurationManager.saveConfig();
    }

    private boolean isValidUrl(String input) {
        return input.matches("https?:\\/\\/(?:localhost:\\d+|[\\w.-]+(?:\\.[\\w.-]+)+)/?");
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
