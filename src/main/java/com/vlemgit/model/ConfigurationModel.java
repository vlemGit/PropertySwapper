package com.vlemgit.model;


import com.vlemgit.service.ConfigurationManager;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class ConfigurationModel {

    private ConfigurationManager configurationManager;
    private List<String> urlList;
    private List<String> directoryList;
    private List<String> performanceList;

    public static final String DIRECTORYPATH_KEY = "DirectoryPath";
    public static final String URLLINKS_KEY = "UrlLinks";
    public static final String PERFORMANCE_KEY = "Performance";
    public static final String TREEVIEWFILTER_KEY = "TreeViewFilter";
    public static final String REGEX_KEY = "Regex";

    public ConfigurationModel(String configFilePath) throws IOException {
        configurationManager = new ConfigurationManager(configFilePath);
        loadSettings();
    }

    private void loadSettings() {
        urlList = configurationManager.getSettings().getOrDefault(URLLINKS_KEY, new ArrayList<>());
        directoryList = configurationManager.getSettings().getOrDefault(DIRECTORYPATH_KEY, new ArrayList<>());
        performanceList = configurationManager.getSettings().getOrDefault(PERFORMANCE_KEY, new ArrayList<>());
    }

    public List<String> getUrlList() {
        return urlList;
    }

    public List<String> getDirectoryList() {
        return directoryList;
    }

    public List<String> getPerformanceList() {
        return performanceList;
    }

    public void addUrl(String url) {
        urlList.add(url);
        configurationManager.setSetting(URLLINKS_KEY, "", url);
    }

    public boolean isValidUrl(String url) {
        return url.matches("https?:\\/\\/(?:localhost:\\d+|[\\w.-]+(?:\\.[\\w.-]+)+)/?");
    }

    public void saveSettings() {
        configurationManager.saveConfig();
    }
}

