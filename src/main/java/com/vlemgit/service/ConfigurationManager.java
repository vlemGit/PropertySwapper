package com.vlemgit.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.scene.control.Alert;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ConfigurationManager {
    private final File configFile;
    private final Map<String, List<String>> settings;
    private final ObjectMapper objectMapper;

    public ConfigurationManager(String filePath) {
        this.configFile = new File(filePath);
        this.objectMapper = new ObjectMapper();
        this.settings = loadConfig();

    }

    public Map<String, List<String>> loadConfig() {
        if (!configFile.exists()) {
            return Collections.emptyMap();
        }

        try {
            return objectMapper.readValue(configFile, new TypeReference<>() {});

        } catch (IOException e) {
            e.printStackTrace();
        }
        return Collections.emptyMap();
    }

    public Map<String, List<String>> getSettings() {
        return settings;
    }

    public void setSetting(String key, String previousValue, String newRowValue) {
        if(previousValue.isEmpty()){
            settings.put(key, List.of(newRowValue));
        } else {
            List<String> values = settings.get(key);
            int index = values.indexOf(previousValue);
            values.set(index, newRowValue);
        }
        saveConfig();
    }

    public void saveConfig() {
        try {
            if("DefaultSettings".equals(configFile.getName())){
                showAlert("Default config file", "If you modify the default setting file, when you'll add a new config it will copy the default file, that's maybe something you want to avoid ?");
                System.out.println("tu change le default !!!");
            }
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(configFile, settings);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public File getConfigFile(){
        return this.configFile;
    }
}
