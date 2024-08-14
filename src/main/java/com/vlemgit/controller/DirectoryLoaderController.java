package com.vlemgit.controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.vlemgit.model.PropertyFile;
import com.vlemgit.model.PropertyLine;
import com.vlemgit.service.DirectoryChooserUtil;
import com.vlemgit.service.PropertyFileLoaderUtil;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Window;

public class DirectoryLoaderController {

    @FXML
    private TreeView<String> fileTreeView;
    @FXML
    private Button directoryLoaderButton;
    @FXML
    private TableView<PropertyLine> propertyTableView;
    @FXML
    private TableColumn<PropertyLine, String> indexColumn;
    @FXML
    private TableColumn<PropertyLine, String> keyColumn;
    @FXML
    private TableColumn<PropertyLine, String> valueColumn;
    @FXML
    private ComboBox<String> filterComboBox;

    private Path directoryPathSystemLocation;

    private Path currentPropertyFile;

    private PropertyFile currentPropertyFileContent;

    private File selectedDirectoryFile;

    @FXML
    private void directoryLoader() {
        Window window = directoryLoaderButton.getScene().getWindow();
        this.selectedDirectoryFile = DirectoryChooserUtil.chooseDirectory(window);
        if (this.selectedDirectoryFile != null) {
            this.directoryPathSystemLocation = selectedDirectoryFile.toPath();
            try {
                displayDirectoryAsTreeView();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

/*     private void directoryChooser() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select Directory");
        this.selectedDirectoryFile = directoryChooser.showDialog(directoryLoaderButton.getScene().getWindow());
    } */

    private void displayDirectoryAsTreeView() throws IOException {
        List<Path> propertiesFiles = fetchPropertyFilesInSelectedDirectory();
        TreeItem<String> rootItem = new TreeItem<>(this.directoryPathSystemLocation.toString());
        rootItem.setExpanded(true);
        fileTreeView.setRoot(rootItem);

        for (Path file : propertiesFiles) {
            Path relativizedPath = this.directoryPathSystemLocation.relativize(file);
            addPathToTreeView(rootItem, relativizedPath);
        }

        fileTreeView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null && newSelection.isLeaf()) {
                String selectedFileAbsolutePath = getFullPath(newSelection, rootItem);
                this.currentPropertyFile = this.directoryPathSystemLocation.resolve(selectedFileAbsolutePath);
                try {
                    this.currentPropertyFileContent = PropertyFileLoaderUtil.load(this.currentPropertyFile);
                    displayFileProperties();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });// also add a filter on the names ? of the files
    }

    private List<Path> fetchPropertyFilesInSelectedDirectory() throws IOException {
        if (this.directoryPathSystemLocation == null) {
            throw new IllegalArgumentException("Directory path cannot be null");
        }

        try (Stream<Path> stream = Files.walk(this.directoryPathSystemLocation)) {
            return stream
                    .filter(Files::isRegularFile)
                    .filter(path -> path.toString().endsWith(".properties"))
                    .sorted()
                    .collect(Collectors.toList());
        }
    }

    private void displayFileProperties() {

    ObservableList<PropertyLine> propertyLines = FXCollections.observableArrayList(this.currentPropertyFileContent.getLines());
    keyColumn.setSortable(true);

    indexColumn.setCellValueFactory(new PropertyValueFactory<>("index"));
    keyColumn.setCellValueFactory(new PropertyValueFactory<>("key"));
    valueColumn.setCellValueFactory(new PropertyValueFactory<>("value"));

    FilteredList<PropertyLine> filteredData = new FilteredList<>(propertyLines, p -> true);
    propertyTableView.setItems(filteredData);

    filterComboBox.setItems(FXCollections.observableArrayList("All", "URL"));
    filterComboBox.setValue("All");

    filterComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
        filteredData.setPredicate(propertyLine -> {
            if (("All").equals(newValue)) {
                return true;
            } else if (("URL").equals(newValue)) {
                return propertyLine.getValue().contains("http://") || propertyLine.getValue().contains("https://");
            }
            return true;
        });
    });

    propertyTableView.setEditable(true);
    keyColumn.setCellFactory(TextFieldTableCell.forTableColumn());
    valueColumn.setCellFactory(TextFieldTableCell.forTableColumn());

    keyColumn.setOnEditCommit(event -> {
        PropertyLine propertyLine = event.getRowValue();
        propertyLine.setKey(event.getNewValue());
        saveChanges();
    });

    valueColumn.setOnEditCommit(event -> {
        PropertyLine propertyLine = event.getRowValue();
        propertyLine.setValue(event.getNewValue());
        saveChanges();
    });

    }

    private void saveChangedPropertiesToFile(Path file, PropertyFile propertyFile) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(file)) {
            List<PropertyLine> lines = new ArrayList<>(propertyFile.getLines());
            int numberOfLines = lines.size();

            for (int i = 0; i < numberOfLines; i++) {
                PropertyLine line = lines.get(i);
                writer.write(line.toString());

                if (i < numberOfLines - 1) {// while not EOF we add new line
                    writer.newLine();
                }
            }
        }
    }

    private void addPathToTreeView(TreeItem<String> rootItem, Path path) {
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

    private void saveChanges() {
        try {
            saveChangedPropertiesToFile(this.currentPropertyFile, this.currentPropertyFileContent);
        } catch (IOException ex) {
            ex.printStackTrace();
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
        return this.directoryPathSystemLocation.toString() + "\\" + fullPath.toString();
    }

}
