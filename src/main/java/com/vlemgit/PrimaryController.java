package com.vlemgit;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;

public class PrimaryController {
    @FXML
    private TreeView<String> fileTreeView;

    @FXML
    private VBox propertyEditorVBox;

    @FXML
    private Button chooseDirectoryButton;

    @FXML
    private ScrollPane propertyEditorScrollPane;

    private Map<String, Map<String, String>> propertiesMap = new HashMap<>();

    private Path directoryFileSystemLocation;

    private Path currentPropertyFile;

    private Map<String, String> currentPropertyContent;

    private List<String> originalFileLines = new ArrayList<>();

    @FXML
    private void chooseDirectoryHandler() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select Directory");
        File selectedDirectoryFile = directoryChooser.showDialog(chooseDirectoryButton.getScene().getWindow());
        if (selectedDirectoryFile != null) {
            //clear window ? to avoid exception when we re choose directory
            directoryFileSystemLocation = selectedDirectoryFile.toPath();
            try {
                displayDirectoryAsTreeView(directoryFileSystemLocation);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void reloadPropertyFileContent(Path file) throws IOException{
    Map<String, String> propertiesMap = fetchPropertyFileContent(file);
    propertyEditorVBox.getChildren().clear();

    for (Map.Entry<String, String> entry : propertiesMap.entrySet()) {
        String key = entry.getKey();
        String value = entry.getValue();
        
        TextField textField = new TextField(value);
        textField.setId(key);
        propertyEditorVBox.getChildren().add(textField);
    }
    }

    private void savePropertiesToFile(Path file) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(file)) {
            for (Map.Entry<String, String> entry : this.currentPropertyContent.entrySet()) {
                writer.write(entry.getKey().trim() + "=" + entry.getValue().trim());
                writer.newLine();
            }
        }
    }

    private void displayDirectoryAsTreeView(Path directory) throws IOException {
        List<Path> propertiesFiles = fetchPropertyFilesInSelectedDirectory(directory);
        TreeItem<String> rootItem = new TreeItem<>(directory.toString());
        rootItem.setExpanded(true);
        fileTreeView.setRoot(rootItem);
        propertiesMap.clear();

        for (Path file : propertiesFiles) {
            Path relativizedPath = directory.relativize(file);
            addPathToTree(rootItem, relativizedPath);
        }

        fileTreeView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null && newSelection.isLeaf()) {
                String selectedFileRelativePath = getFullPath(newSelection, rootItem);
                Path fullPath = directoryFileSystemLocation.resolve(selectedFileRelativePath);
                this.currentPropertyFile= fullPath;
                try{
                this.currentPropertyContent = fetchPropertyFileContent(fullPath);
                loadFileProperties(this.currentPropertyContent);
                } catch(IOException e){
                    e.printStackTrace();
                }
            }
        });
    }

    private void addPathToTree(TreeItem<String> rootItem, Path path) {
        TreeItem<String> currentItem = rootItem;

        for (Path part : path) {
            String partName = part.toString();
            TreeItem<String> childItem = findChild(currentItem, partName);
            if (childItem == null) {
                childItem = new TreeItem<>(partName);
                currentItem.getChildren().add(childItem);
            }
            currentItem = childItem;
        }
    }

    private TreeItem<String> findChild(TreeItem<String> parent, String name) {
        for (TreeItem<String> child : parent.getChildren()) {
            if (child.getValue().equals(name)) {
                return child;
            }
        }
        return null;
    }

    private List<Path> fetchPropertyFilesInSelectedDirectory(Path dir) throws IOException {
        if (dir == null) {
            throw new IllegalArgumentException("Directory path cannot be null");
        }

        try (Stream<Path> stream = Files.walk(dir)) {
            return stream
                    .filter(Files::isRegularFile)
                    .filter(path -> path.toString().endsWith(".properties"))
                    .sorted()
                    .collect(Collectors.toList());
        }
    }

    private Map<String, String> fetchPropertyFileContent(Path file) throws IOException {
        Map<String, String> propertiesMapcontent = new LinkedHashMap<>();
        
        List<String> lines = Files.readAllLines(file);
        
        for (String line : lines) {
            line = line.trim();
            
            if (!line.isEmpty() && line.contains("=")) {
                String[] keyValue = line.split("=", 2);
                
                if (keyValue.length == 2) {
                    String key = keyValue[0].trim();
                    String value = keyValue[1].trim();                  
                    propertiesMapcontent.put(key, value);
                }

            }
        }
        return propertiesMapcontent;
    }

     private void loadFileProperties(Map<String, String> properties) {
        propertyEditorVBox.getChildren().clear();

    if (properties != null) {
        int rowIndex = 0;
        for (Map.Entry<String, String> entry : properties.entrySet()) {
            HBox hBox = new HBox();
            hBox.setSpacing(25);

            TextField keyField = new TextField(entry.getKey());
            keyField.setPrefWidth(250);
            Label separatorLabel = new Label("=");
            TextField valueField = new TextField(entry.getValue());
            valueField.setPrefWidth(500);

            if (rowIndex % 2 == 0) {
                hBox.setStyle("-fx-background-color: #f0f0f0;");
            } else {
                hBox.setStyle("-fx-background-color: #ffffff;");
            }

            keyField.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) ->{
                if (!newValue.trim().equals(oldValue.trim())) {
                    String value = this.currentPropertyContent.remove(oldValue.trim());
                    this.currentPropertyContent.put(newValue.trim(), value);
                    try {
                        System.out.println("old value : " +oldValue + " changed to => " + newValue);
                        savePropertiesToFile(this.currentPropertyFile);
                    } catch (IOException ex) {
                    }
                }
                
            });

            valueField.textProperty().addListener((observable, oldValue, newValue) ->{
                if (!newValue.trim().equals(oldValue.trim())) {
                    String key = valueField.getId();
                    this.currentPropertyContent.put(key, newValue.trim());
                    try {
                        System.out.println("old value : " +oldValue + " changed to => " + newValue);
                        savePropertiesToFile(this.currentPropertyFile);
                    } catch (IOException ex) {
                    }
                }   
        });

            hBox.getChildren().addAll(keyField,separatorLabel, valueField);
            propertyEditorVBox.getChildren().add(hBox);
        }
    }
    }

    private String getFullPath(TreeItem<String> item, TreeItem<String> rootItem) {
        StringBuilder fullPath = new StringBuilder();
        while (item != rootItem) {
            fullPath.insert(0, item.getValue());
            item = item.getParent();
            if (item != rootItem) {
                fullPath.insert(0, File.separator);
            }
        }
        return this.directoryFileSystemLocation.toString() + "\\" +fullPath.toString();
    }

}
