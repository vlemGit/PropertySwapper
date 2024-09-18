package com.vlemgit.model;


import com.vlemgit.service.ConfigurationManager;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class ConfigurationModel {

    private ConfigurationManager configurationManager;
    private List<String> urlList;
    private List<String> directoryList;
    private List<String> performanceList;
    private List<String> treeViewFilterList;
    private List<String> regexList;

    private List<String> configFileNames;

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
        directoryList = configurationManager.getSettings().getOrDefault(DIRECTORYPATH_KEY, new ArrayList<>());
        urlList = configurationManager.getSettings().getOrDefault(URLLINKS_KEY, new ArrayList<>());
        performanceList = configurationManager.getSettings().getOrDefault(PERFORMANCE_KEY, new ArrayList<>());
        treeViewFilterList = configurationManager.getSettings().getOrDefault(TREEVIEWFILTER_KEY, new ArrayList<>());
        regexList = configurationManager.getSettings().getOrDefault(REGEX_KEY, new ArrayList<>());
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

    public List<String> getTreeViewFilterList() {
        return treeViewFilterList;
    }

    public List<String> getRegexList() {
        return regexList;
    }

    public void setUrl(String url) {
        urlList.add(url);
        configurationManager.setSetting(URLLINKS_KEY, String.valueOf(urlList), url);
    }

    public void addUrl(String url) {
        urlList.add(url);
        configurationManager.addSetting(URLLINKS_KEY, url);
    }

    public void setDirectory(String directory){
        directoryList.add(directory);
        configurationManager.setSetting(DIRECTORYPATH_KEY, String.valueOf(directoryList), directory);
    }

    public void setPerformance(String performance){
        performanceList.add(performance);
        configurationManager.setSetting(PERFORMANCE_KEY, String.valueOf(performanceList), performance);
    }

    public void setTreeViewFilter(String treeViewFilter){
        treeViewFilterList.add(treeViewFilter);
        configurationManager.setSetting(TREEVIEWFILTER_KEY, String.valueOf(treeViewFilterList), treeViewFilter);
    }

    public void setRegex(String regex){
        regexList.add(regex);
        configurationManager.setSetting(REGEX_KEY, String.valueOf(regexList), regex);
    }

    public void saveSettings() {
        configurationManager.saveConfig();
    }

    public List<String> getConfigFileNames(Path settingsConfigPathFolder) throws IOException {

        List<File> settingFiles = listSettingFilesBasedOnCustomFolder(settingsConfigPathFolder);
        configFileNames = settingFiles.stream()
                .map(File::getName)
                .toList();
        return configFileNames;
    }

    private List<File> listSettingFilesBasedOnCustomFolder(Path settingFolderPath) throws IOException {
        try (Stream<Path> stream = Files.walk(settingFolderPath)) {
            return stream
                    .filter(Files::isRegularFile)
                    .filter(path -> path.toString().endsWith(".conf"))
                    .filter(path -> !path.toString().contains("MetaSettings"))
                    .sorted()
                    .map(Path::toFile)
                    .toList();
        }
    }
}

