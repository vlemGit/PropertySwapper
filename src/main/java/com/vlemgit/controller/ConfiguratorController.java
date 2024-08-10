package com.vlemgit.controller;

import java.io.File;

import com.vlemgit.service.ConfigurationManager;
import com.vlemgit.service.DirectoryChooserUtil;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Window;

public class ConfiguratorController {

    @FXML
    private TextField directoryPathField;

    private ConfigurationManager configurationManager;

    @FXML
    public void initialize() {
        configurationManager = new ConfigurationManager("src\\main\\resources\\com\\vlemgit\\settings.conf");
        loadSettings();
    }

    @FXML
    private void openDirectoryChooser(){
         Window window = directoryPathField.getScene().getWindow();
         File selectedDirectory = DirectoryChooserUtil.chooseDirectory(window);

         if (selectedDirectory != null) {
            directoryPathField.setText(selectedDirectory.getAbsolutePath());
        }
    }

    @FXML
    private void saveSettings() {
        configurationManager.setSetting("directoryPath", directoryPathField.getText());
        // Sauvegardez d'autres paramètres si nécessaire
        configurationManager.saveConfig();
    }
    
    private void loadSettings() {
        directoryPathField.setText(configurationManager.getSetting("directoryPath"));
        // Chargez d'autres paramètres si nécessaire
    }
}
