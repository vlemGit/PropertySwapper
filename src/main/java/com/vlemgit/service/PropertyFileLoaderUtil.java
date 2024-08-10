package com.vlemgit.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import com.vlemgit.model.PropertyFile;
import com.vlemgit.model.PropertyLine;

public class PropertyFileLoaderUtil {

    private PropertyFileLoaderUtil(){
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static PropertyFile load(Path path) throws IOException {
        PropertyFile propertyFile = new PropertyFile();
        try (BufferedReader reader = Files.newBufferedReader(path)) {
            String line;
            int lineNumber = 1;

            while ((line = reader.readLine()) != null) {
                boolean isCommented = line.trim().startsWith("#");
                String content = line.trim();

                if (content.contains("=")) {
                    String[] keyValue = content.split("=", 2);
                    propertyFile.addLine(new PropertyLine(lineNumber, keyValue[0].trim(), keyValue[1].trim(), isCommented));
                } else {
                    if (isCommented) {
                        propertyFile.addLine(new PropertyLine(lineNumber, "#", "", isCommented));
                    }
                    propertyFile.addLine(new PropertyLine(lineNumber, "", "", isCommented));
                }

                lineNumber++;
            }
        }

        return propertyFile;
    }
}