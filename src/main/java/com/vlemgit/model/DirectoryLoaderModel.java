package com.vlemgit.model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class DirectoryLoaderModel {

    private Path directoryPathSystemLocation;
    private PropertyFile currentPropertyFileContent;

    private Path currentPropertyFile;

    public void setDirectoryPath(Path path) {
        this.directoryPathSystemLocation = path;
    }

    public Path getDirectoryPath() {
        return directoryPathSystemLocation;
    }

    public List<Path> fetchPropertyFilesInSelectedDirectory() throws IOException {
        if (directoryPathSystemLocation == null) {
            throw new IllegalArgumentException("Directory path cannot be null");
        }

        try (Stream<Path> stream = Files.walk(directoryPathSystemLocation)) {
            return stream
                    .filter(Files::isRegularFile)
                    .filter(path -> path.toString().endsWith(".properties"))
                    .sorted()
                    .toList();
        }
    }

    public void loadPropertyFile(Path file) throws IOException {
        List<String> fileLines = Files.readAllLines(file);

        List<PropertyLine> lines = IntStream.range(0, fileLines.size())
                .mapToObj(i -> {
                    String line = fileLines.get(i);
                    boolean isCommented = line.trim().startsWith("#");

                    if (line.trim().isEmpty()) {
                        return new PropertyLine(i, "", "", false);
                    }
                    if (isCommented) {
                        String trimmedLine = line.substring(1).trim();
                        if (trimmedLine.isEmpty()) {
                            return new PropertyLine(i, "#", "", true);
                        }
                        String[] parts = trimmedLine.split("=", 2);
                        if (parts.length == 2) {
                            return new PropertyLine(i, "#" + parts[0].trim(), parts[1].trim(), true);
                        }
                        return new PropertyLine(i, "#" + trimmedLine, "????Whats the correct value????", true);
                    }

                    String[] parts = line.split("=", 2);
                    if (parts.length == 2) {
                        return new PropertyLine(i, parts[0].trim(), parts[1].trim(), false);
                    }
                    return new PropertyLine(i, line.trim(), "", false);
                })
                .toList();

        PropertyFile propertyFile = new PropertyFile();
        lines.forEach(propertyFile::addLine);

        this.currentPropertyFileContent = propertyFile;
    }

    public PropertyFile getCurrentPropertyFileContent() {
        return currentPropertyFileContent;
    }

    public void setCurrentPropertyFileContent(PropertyFile propertyFile){
        this.currentPropertyFileContent = propertyFile;
    }

    public Path getCurrentPropertyFile(){
        return currentPropertyFile;
    }

    public void setCurrentPropertyFile(Path propertyFile){
        this.currentPropertyFile = propertyFile;
    }

    public void updatePropertyLine(int index, PropertyLine updatedLine) {
        PropertyFile propertyFile = getCurrentPropertyFileContent();
        propertyFile.updateLine(index, updatedLine.getKey(), updatedLine.getValue(), updatedLine.isCommented());
    }
}
