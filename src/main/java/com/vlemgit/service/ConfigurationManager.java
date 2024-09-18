package com.vlemgit.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Pattern;

import javafx.scene.control.Alert.AlertType;

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

    public List<String> getSettingByName(String name){
        return settings.get(name);
    }

    public void setSetting(String key, String previousValue, String newRowValue) {
        if(previousValue.isEmpty()){
            settings.put(key, List.of(newRowValue));
        } else {
            List<String> values = settings.get(key);
            int index = values.indexOf(previousValue);
            values.set(index, newRowValue);
        }
    }

    public void addSetting(String key, String newRowValue) {
            settings.put(key, List.of(newRowValue));
    }

    public void saveConfig() {
        try {
            if("DefaultSettings".equals(configFile.getName())){
                new Alert(AlertType.ERROR, "Default config file", "If you modify the default setting file, when you'll add a new config it will copy the default file, that's maybe something you want to avoid ?");
                System.out.println("tu change le default !!!");
            }
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(configFile, settings);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

/*    private void updateConnfigFile(String filePath){
        this.configFile = new File(filePath);
    }*/


    public File getConfigFile(){
        return this.configFile;
    }

    public void createNewConfigFile(String newFileName) {
        Path sourcePath = Paths.get("src\\main\\resources\\settings\\DefaultSettings.conf");
        Path destinationPath = Paths.get("src\\main\\resources\\settings\\" + newFileName + ".conf");
        try {
            Files.copy(sourcePath, destinationPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isValidUrl(String url) {
        return url.matches("https?:\\/\\/(?:localhost:\\d+|[\\w.-]+(?:\\.[\\w.-]+)+)/?");
    }

    public boolean isValidUrlAndSurroundedWellByArrows(String newRowValue, String previousValue) {

        String groupSeparator = "->";
        String[] newRowgroups = newRowValue.split(Pattern.quote(groupSeparator));

        for (String group : newRowgroups) {
            if (!group.trim().isEmpty() && !isValidUrl(group.trim())) {
                return false;
            }
        }
        if(!"".equals(previousValue)){
            String[] previousValuegroups = previousValue.split(Pattern.quote(groupSeparator));
            for (String group : previousValuegroups) {
                if (!group.trim().isEmpty() && !isValidUrl(group.trim())) {
                    return false;
                }
            }
        }
        return true;
    }
}
