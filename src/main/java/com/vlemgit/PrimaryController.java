package com.vlemgit;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.PropertyFile;
import com.PropertyLine;

import javafx.beans.value.ObservableValue;
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

    private Path currentPropertyFile;

    private PropertyFile currentPropertyContent;

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

    private void saveChangedPropertiesToFile(Path file, PropertyFile propertyFile) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(file)) {
            List<PropertyLine> lines = new ArrayList<>(propertyFile.getLines());
            int numberOfLines = lines.size();
            
            for (int i = 0; i < numberOfLines; i++) {
                PropertyLine line = lines.get(i);
                writer.write(line.toString());
                
                if (i < numberOfLines - 1) {// if not EOF
                    writer.newLine();
                }
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
                String selectedFileAbsolutePath = getFullPath(newSelection, rootItem);
                Path fullPath = directoryFileSystemLocation.resolve(selectedFileAbsolutePath);
                this.currentPropertyFile = fullPath;
                try {
                    PropertyFile propertyFileContent = fetchPropertyFileContent(fullPath);
                    this.currentPropertyContent = propertyFileContent;
                    loadFileProperties(propertyFileContent);
                } catch (IOException e) {
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

    private PropertyFile fetchPropertyFileContent(Path file) throws IOException {
        PropertyFile propertyFile = new PropertyFile();
    this.originalFileLines.clear();

    try (BufferedReader reader = Files.newBufferedReader(file)) {
        String line;
        int lineNumber = 1;

        while ((line = reader.readLine()) != null) {
            line = line.trim();

            boolean isCommented = line.startsWith("#");

            if (line.contains("=")) {
                String[] keyValue = line.split("=", 2);
                if (keyValue.length == 2) {
                    String key = keyValue[0].trim();
                    String value = keyValue[1].trim();
                    propertyFile.addLine(new PropertyLine(lineNumber, key, value, isCommented));
                }
            } else {
                propertyFile.addLine(new PropertyLine(lineNumber, "", "", isCommented));
            }

            lineNumber++;
        }
    }

    return propertyFile;
}

     private void loadFileProperties(PropertyFile propertyFileContent) {
       propertyEditorVBox.getChildren().clear();

    if (propertyFileContent != null) {
        for (PropertyLine propertyLine : propertyFileContent.getLines()) {
            if (propertyLine.getKey().isEmpty()) {
                continue;
            }

            HBox hBox = new HBox();
            hBox.setSpacing(25);

            Label numOfLine = new Label(String.valueOf(propertyLine.getLineNumber()));
            numOfLine.setPrefWidth(25);
            TextField keyField = new TextField(propertyLine.getKey());
            keyField.setPrefWidth(250);
            Label separatorLabel = new Label("=");
            TextField valueField = new TextField(propertyLine.getValue());
            valueField.setPrefWidth(500);

            if (propertyLine.getLineNumber() % 2 == 0) {
                hBox.setStyle("-fx-background-color: #f0f0f0;");
            } else {
                hBox.setStyle("-fx-background-color: #ffffff;");
            }

            keyField.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
                if (!newValue.trim().equals(oldValue.trim())) {
                    int lineNumber = propertyLine.getLineNumber();
                    PropertyLine currentPropertyLine = currentPropertyContent.getLines().stream()
                            .filter(l -> l.getLineNumber() == lineNumber && l.getKey().equals(oldValue.trim()))
                            .findFirst()
                            .orElse(null); // can do simpler same for the valueListener ... by just getting the lineNumber
            
                    if (currentPropertyLine != null) {
                        currentPropertyContent.updateLine(currentPropertyLine.getLineNumber(), newValue.trim(), currentPropertyLine.getValue(), currentPropertyLine.isCommented());
            
                        try {
                            
                            saveChangedPropertiesToFile(this.currentPropertyFile, this.currentPropertyContent);
                            System.out.println("old value : " + oldValue + " changed to => " + newValue);
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            });

            valueField.textProperty().addListener((observable, oldValue, newValue) -> {
                if (!newValue.trim().equals(oldValue.trim())) {
                    int lineNumber = propertyLine.getLineNumber();
                    PropertyLine line = currentPropertyContent.getLines().stream()
                            .filter(l -> l.getLineNumber() == lineNumber && l.getKey().equals(keyField.getText().trim()))
                            .findFirst()
                            .orElse(null);
            
                    if (line != null) {
                        currentPropertyContent.updateLine(line.getLineNumber(), line.getKey(), newValue.trim(), line.isCommented());
            
                        try {
                            System.out.println("old value : " + oldValue + " changed to => " + newValue);
                            saveChangedPropertiesToFile(this.currentPropertyFile, this.currentPropertyContent);
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            });

            hBox.getChildren().addAll(numOfLine, keyField, separatorLabel, valueField);
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
