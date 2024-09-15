package com.vlemgit.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;


import com.vlemgit.model.ConfigurationModel;
import com.vlemgit.service.ConfigurationManager;
import com.vlemgit.service.DirectoryChooserUtil;
import com.vlemgit.service.EditableHighlightArrowListCell;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.stage.Window;
import javafx.util.StringConverter;
import javafx.util.converter.DefaultStringConverter;

public class ConfiguratorController {

    @FXML
    private ListView<String> listViewDirectoryPath;

    @FXML
    private ListView<String> listViewUrlLinks;

    @FXML
    private TextField newUrlField;

    @FXML
    private ListView<String> listViewPerformance;

    @FXML
    private ListView<String> listForTreeViewFilter;

    @FXML
    private ListView<String> listForRegexFieldView;

    @FXML
    private TextField newPerfFolderField;

    @FXML
    private TextField newTreeViewFilterText;

    @FXML
    private TextField newRegexField;

    @FXML
    private ComboBox<String> myComboBox;

    @FXML
    private Label activeFileLabel;

    private ObservableList<String> observableDirectoryPath;

    private ObservableList<String> observableUrlItems;

    private ObservableList<String> observablePerfFolderItems;

    private ObservableList<String> observableTreeViewFilterItems;

    private ObservableList<String> observableRegexView;

    private ConfigurationManager metaConfigurationManager;
    private ConfigurationManager configurationManager;


    
    public static final String DEFAULT_CONFIG_FILE ="DefaultConfigFile";

    private Path settingFolderPath;

    private ConfigurationModel configurationModel;

    @FXML
    public void initialize() {
        try {
            configurationModel = new ConfigurationModel("src\\main\\resources\\com\\vlemgit\\settings\\MetaSettings.conf");
            initializeUI();
        } catch (IOException e) {
            showError("Failed to load configuration", e.getMessage());
        }
    }

    private void initializeUI() {
        Platform.runLater(() -> {
            try {
                setupComboBox();
                //activeFileLabel.setText("Active config file: " + configurationModel.getDefaultConfigFileName());
                updateListViewItems();
            } catch (IOException e) {
                showError("Failed to initialize UI", e.getMessage());
            }
        });
    }

    private void setupComboBox() throws IOException {
        //List<String> settingFiles = configurationModel.getSettingFiles(Path.of("src\\main\\resources\\com\\vlemgit\\settings"));
        ObservableList<String> items = FXCollections.observableArrayList();
        items.add("Add new config");
        //items.addAll(settingFiles);
        myComboBox.setItems(items);

        myComboBox.getSelectionModel().selectedItemProperty().addListener(selectionChangeListener);
    }

    private void updateListViewItems() {
        //listViewDirectoryPath.setItems(FXCollections.observableArrayList(configurationModel.getDirectoryPaths()));
        //listViewUrlLinks.setItems(FXCollections.observableArrayList(configurationModel.getUrls()));
    }

    private final ChangeListener<String> selectionChangeListener = (observable, oldValue, newValue) -> {
        if (newValue != null) {
            if ("Add new config".equals(newValue)) {
                // Logique pour ajouter un nouveau fichier de configuration
            } else {
                // Changer le fichier de configuration par défaut et mettre à jour l'affichage
            }
        }
    };

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void openDirectoryChooser() {
        Window window = listViewDirectoryPath.getScene().getWindow();
        File selectedDirectory = DirectoryChooserUtil.chooseDirectory(window);

        if (selectedDirectory != null) {
            this.observableDirectoryPath = FXCollections.observableArrayList(selectedDirectory.getAbsolutePath());
            listViewDirectoryPath.setItems(observableDirectoryPath);
            //saveSettings(DIRECTORYPATH_KEY, "", selectedDirectory.getAbsolutePath());
        }
    }

    @FXML
    private void addUrls() {
        String newItem = newUrlField.getText();
        /*if (isValidUrl(newItem)) {
            observableUrlItems.add(newItem);
            newUrlField.clear();
            saveSettings(URLLINKS_KEY, "", newItem);
        } else {
            showAlert("Invalid format", "The input must be in the format : \n url1 \n url1->url2 \n url1->url2->url3->urlX... ");
        }*/
    }

    @FXML
    private void addPerformanceFolder() {
        String newFolder = newPerfFolderField.getText();
        /*if (newFolder.isEmpty()) {
            showAlert("Invalid input", "The input is empty");
        } else {
            observablePerfFolderItems.add(newFolder);
            newPerfFolderField.clear();
            saveSettings(PERFORMANCE_KEY, "", newFolder);
        }*/
    }

    @FXML
    private void addFilterForTreeView() {
        String newFilter = newTreeViewFilterText.getText();
        /*if (newFilter.isEmpty()) {
            showAlert("Invalid input", "The input is empty");
        } else {
            observableTreeViewFilterItems.add(newFilter);
            newTreeViewFilterText.clear();
            saveSettings(TREEVIEWFILTER_KEY, "", newFilter);
        }*/
    }

    @FXML
    private void addRegex() {
        String newRegex = newRegexField.getText();
        /*if (newRegex.isEmpty()) {
            showAlert("Invalid input", "The input is empty");
        } else {
            if (listForRegexFieldView.getItems().isEmpty()) {
                observableRegexView.add(newRegex);
                newRegexField.clear();
                saveSettings(REGEX_KEY, "", newRegex);
            } else {
                showAlert("Already Setup", "We can only use one regex, please modify the existing one if needed");
            }
        }*/
    }

    @FXML
    private void importSettings() {

    }

    @FXML
    private void exportSettings() {

    }


/*    @FXML
    public void initialize() throws IOException {

        // initialize for the combobox config event changer


        metaConfigurationManager = new ConfigurationManager("src\\main\\resources\\com\\vlemgit\\settings\\MetaSettings.conf");
        settingFolderPath = Path.of("src\\main\\resources\\com\\vlemgit\\settings");

        configurationManager = new ConfigurationManager(metaConfigurationManager.getSettings().get(DEFAULT_CONFIG_FILE).get(0));

        List<File> settingFiles = listSettingFilesBasedOnCustomFolder(settingFolderPath);
        List<String> settingFileName = settingFiles.stream()
                .map(File::getName)
                .toList();

        Platform.runLater(() -> {
            ObservableList<String> items = FXCollections.observableArrayList();
            items.add("Add new config");
            items.addAll(settingFileName);

            if (!myComboBox.getItems().isEmpty()) {
                myComboBox.getSelectionModel().selectedItemProperty().removeListener(selectionChangeListener);
            }
            myComboBox.setItems(items);

            myComboBox.getSelectionModel().selectedItemProperty().addListener(selectionChangeListener);
        });

        String defaultFilePath = metaConfigurationManager.getSettings().get(DEFAULT_CONFIG_FILE).get(0);
        String defaultFileName = Path.of(defaultFilePath).getFileName().toString();
        Platform.runLater(() -> activeFileLabel.setText("Active config file: " + defaultFileName));


        // directory path
        List<String> directoryPathSetting = configurationManager.getSettings().get(DIRECTORYPATH_KEY);
        if ( directoryPathSetting == null || directoryPathSetting.isEmpty()) {
            this.observableDirectoryPath = FXCollections.observableArrayList();
        } else {
            this.observableDirectoryPath = FXCollections.observableArrayList(directoryPathSetting);
        }

        listViewDirectoryPath.setItems(observableDirectoryPath);

        //url links
        List<String> urlConfSetting = configurationManager.getSettings().get(URLLINKS_KEY);
        if ( urlConfSetting == null || urlConfSetting.isEmpty()) {
            this.observableUrlItems = FXCollections.observableArrayList();
        } else {
            this.observableUrlItems = FXCollections.observableArrayList(urlConfSetting);
        }


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
                saveSettings(URLLINKS_KEY, previousValue, newRowValue);
                if (newRowValue.isEmpty()) {
                    listViewUrlLinks.getItems().remove(event.getIndex());
                    listViewUrlLinks.getSelectionModel().clearSelection();
                }
            } else {
                showAlert("Invalid format", "The input must be in the format : \n url1 \n url1->url2 \n url1->url2->url3->urlX... ");
                listViewUrlLinks.refresh();
            }
        });

        //Performance section
        List<String> perfConfSetting = configurationManager.getSettings().get(PERFORMANCE_KEY);
        if (perfConfSetting == null || perfConfSetting.isEmpty()) {
            this.observablePerfFolderItems = FXCollections.observableArrayList();
        } else {
            this.observablePerfFolderItems = FXCollections.observableArrayList(perfConfSetting);
        }

        listViewPerformance.setItems(observablePerfFolderItems);
        final String[] oldValuePerf = new String[1];

        listViewPerformance.setCellFactory(param -> new TextFieldListCell<>(new StringConverter<>() {
            @Override
            public String toString(String object) {
                return object;
            }

            @Override
            public String fromString(String string) {
                return string;
            }
        }));

        listViewPerformance.autosize();
        newPerfFolderField.setOnAction(event -> addPerformanceFolder());

        listViewPerformance.setOnEditStart(event -> oldValuePerf[0] = listViewPerformance.getItems().get(event.getIndex()));

        listViewPerformance.setOnEditCommit(event -> {
            String previousValue = oldValuePerf[0];
            String newRowValue = event.getNewValue();
            listViewPerformance.getItems().set(event.getIndex(), newRowValue);
            saveSettings(PERFORMANCE_KEY, previousValue, newRowValue);
            if (newRowValue.isEmpty()) {
                listViewPerformance.getItems().remove(event.getIndex());
                listViewPerformance.getSelectionModel().clearSelection();
            }
        });

        // treeview Filter section
        List<String> treeViewFilterConfSetting = configurationManager.getSettings().get(TREEVIEWFILTER_KEY);
        if (treeViewFilterConfSetting == null || treeViewFilterConfSetting.isEmpty()) {
            observableTreeViewFilterItems = FXCollections.observableArrayList();
        } else {
            this.observableTreeViewFilterItems = FXCollections.observableArrayList(treeViewFilterConfSetting);
        }


        listForTreeViewFilter.setItems(observableTreeViewFilterItems);
        final String[] oldValueTreeView = new String[1];

        listForTreeViewFilter.setCellFactory(param -> new TextFieldListCell<>(new StringConverter<>() {
            @Override
            public String toString(String object) {
                return object;
            }

            @Override
            public String fromString(String string) {
                return string;
            }
        }));

        listForTreeViewFilter.autosize();
        newTreeViewFilterText.setOnAction(event -> addFilterForTreeView());

        listForTreeViewFilter.setOnEditStart(event -> oldValueTreeView[0] = listForTreeViewFilter.getItems().get(event.getIndex()));

        listForTreeViewFilter.setOnEditCommit(event -> {
            String newRowValue = event.getNewValue();
            String previousValue = oldValueTreeView[0];
            listForTreeViewFilter.getItems().set(event.getIndex(), newRowValue);
            saveSettings(TREEVIEWFILTER_KEY, previousValue, newRowValue);
            if (newRowValue.isEmpty()) {
                listForTreeViewFilter.getItems().remove(event.getIndex());
                listForTreeViewFilter.getSelectionModel().clearSelection();
            }
        });

        // regex section
        List<String> regexConfSetting = configurationManager.getSettings().get(REGEX_KEY);
        if (regexConfSetting == null || regexConfSetting.isEmpty()) {
            observableRegexView = FXCollections.observableArrayList();
        } else {
            this.observableRegexView = FXCollections.observableArrayList(regexConfSetting);
        }


        listForRegexFieldView.setItems(observableRegexView);
        final String[] oldValueRegexView = new String[1];

        listForRegexFieldView.setCellFactory(param -> new TextFieldListCell<>(new StringConverter<>() {
            @Override
            public String toString(String object) {
                return object;
            }

            @Override
            public String fromString(String string) {
                return string;
            }
        }));

        listForRegexFieldView.autosize();
        newRegexField.setOnAction(event -> addRegex());

        listForRegexFieldView.setOnEditStart(event -> oldValueRegexView[0] = listForRegexFieldView.getItems().get(event.getIndex()));

        listForRegexFieldView.setOnEditCommit(event -> {
            String previousValue = oldValueRegexView[0];
            String newRowValue = event.getNewValue();
            listForRegexFieldView.getItems().set(event.getIndex(), newRowValue);
            saveSettings(REGEX_KEY, previousValue, newRowValue);
            if (newRowValue.isEmpty()) {
                listForRegexFieldView.getItems().remove(event.getIndex());
                listForRegexFieldView.getSelectionModel().clearSelection();
            }
        });
    }

    private final ChangeListener<String> selectionChangeListener = (observable, oldValue, newValue) -> {
        if (newValue != null) {
            if ("Add new config".equals(newValue)) {
                String newFileName = askUserToEnterAFileName();
                Path newConfigFilePath = createNewConfigFile(newFileName);
                metaConfigurationManager.setSetting("DefaultConfigFile", metaConfigurationManager.getSettings().get(DEFAULT_CONFIG_FILE).get(0), String.valueOf(newConfigFilePath));
                try {
                    Platform.runLater(() -> {
                        try {
                            initialize();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            } else {
                metaConfigurationManager.setSetting("DefaultConfigFile", metaConfigurationManager.getSettings().get(DEFAULT_CONFIG_FILE).get(0), settingFolderPath + "\\" + newValue);
                try {
                    Platform.runLater(() -> {
                        try {
                            initialize();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
    };
    private String askUserToEnterAFileName() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("New Configuration File");
        dialog.setHeaderText("Create a new configuration file");
        dialog.setContentText("Please enter the name of the new config file:");


        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            if (result.get().contains(".")) {
                showAlert("Invalid format", "the filename must be correct and without the extension");
            } else {
                return result.get();
            }
        } else {
            showAlert("Invalid format", "the filename must be correct");
        }
        return "";
    }

    private Path createNewConfigFile(String newFileName) {
        Path sourcePath = Paths.get("src\\main\\resources\\com\\vlemgit\\settings\\DefaultSettings.conf");
        Path destinationPath = Paths.get("src\\main\\resources\\com\\vlemgit\\settings\\" + newFileName + ".conf");
        try {
            Files.copy(sourcePath, destinationPath);
            return Path.of(destinationPath.toFile().getPath());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
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
        Window window = listViewDirectoryPath.getScene().getWindow();
        File selectedDirectory = DirectoryChooserUtil.chooseDirectory(window);

        if (selectedDirectory != null) {
            this.observableDirectoryPath = FXCollections.observableArrayList(selectedDirectory.getAbsolutePath());
            listViewDirectoryPath.setItems(observableDirectoryPath);
            saveSettings(DIRECTORYPATH_KEY, "", selectedDirectory.getAbsolutePath());
        }
    }

    @FXML
    private void addUrls() {
        String newItem = newUrlField.getText();
        if (isValidUrl(newItem)) {
            observableUrlItems.add(newItem);
            newUrlField.clear();
            saveSettings(URLLINKS_KEY, "", newItem);
        } else {
            showAlert("Invalid format", "The input must be in the format : \n url1 \n url1->url2 \n url1->url2->url3->urlX... ");
        }
    }

    @FXML
    private void addPerformanceFolder() {
        String newFolder = newPerfFolderField.getText();
        if (newFolder.isEmpty()) {
            showAlert("Invalid input", "The input is empty");
        } else {
            observablePerfFolderItems.add(newFolder);
            newPerfFolderField.clear();
            saveSettings(PERFORMANCE_KEY, "", newFolder);
        }
    }

    @FXML
    private void addFilterForTreeView() {
        String newFilter = newTreeViewFilterText.getText();
        if (newFilter.isEmpty()) {
            showAlert("Invalid input", "The input is empty");
        } else {
            observableTreeViewFilterItems.add(newFilter);
            newTreeViewFilterText.clear();
            saveSettings(TREEVIEWFILTER_KEY, "", newFilter);
        }
    }

    @FXML
    private void addRegex() {
        String newRegex = newRegexField.getText();
        if (newRegex.isEmpty()) {
            showAlert("Invalid input", "The input is empty");
        } else {
            if (listForRegexFieldView.getItems().isEmpty()) {
                observableRegexView.add(newRegex);
                newRegexField.clear();
                saveSettings(REGEX_KEY, "", newRegex);
            } else {
                showAlert("Already Setup", "We can only use one regex, please modify the existing one if needed");
            }
        }
    }

    private void saveSettings(String key, String previousValue, String newRowValue) {
        configurationManager.setSetting(key, previousValue, newRowValue);
        //configurationManager.saveConfig(); // check pour supprimer ce doublon ?
    }

    @FXML
    private void importSettings() {

    }

    @FXML
    private void exportSettings() {

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
    }*/
}
