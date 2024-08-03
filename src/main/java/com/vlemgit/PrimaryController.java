package com.vlemgit;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javafx.fxml.FXML;
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

    @FXML
    private void chooseDirectoryHandler() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select Directory");
        File selectedDirectoryFile = directoryChooser.showDialog(chooseDirectoryButton.getScene().getWindow());
        if (selectedDirectoryFile != null) {
            directoryFileSystemLocation = selectedDirectoryFile.toPath();
            try {
                displayDirectoryAsTreeView(directoryFileSystemLocation);
            } catch (IOException e) {
                e.printStackTrace();
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
                try{
                Map<String, String> propertyContent = fetchPropertyFileContent(fullPath);
                loadFileProperties(propertyContent);
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
        Properties properties = new Properties();
        properties.load(Files.newBufferedReader(file));

        Map<String, String> propertiesMap = new HashMap<>();
        for (String key : properties.stringPropertyNames()) {
            propertiesMap.put(key, properties.getProperty(key));
        }
        return propertiesMap;
    }

     private void loadFileProperties(Map<String, String> properties) {
        propertyEditorVBox.getChildren().clear();

        if (properties != null) {
            for (Map.Entry<String, String> entry : properties.entrySet()) {
                HBox hBox = new HBox();
                Label label = new Label(entry.getKey() + "=");
                TextField textField = new TextField(entry.getValue());
                //Button saveButton = new Button("Save");

                //saveButton.setOnAction(event -> saveProperty(filePath, entry.getKey(), textField.getText()));
                //hBox.getChildren().addAll(label, textField, saveButton);
                hBox.getChildren().addAll(label, textField);
                propertyEditorVBox.getChildren().add(hBox);
            }
        }
    }

    private void saveProperty(String filePath, String key, String value) {
        System.out.println("Saving property " + key + "=" + value + " to file " + filePath);
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
