package com;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class PropertyFileLoader {
    public PropertyFile load(Path path) throws IOException {
        PropertyFile propertyFile = new PropertyFile();
        try (BufferedReader reader = Files.newBufferedReader(path)) {
            String line;
            int lineNumber = 1;
            
            while ((line = reader.readLine()) != null) {
                boolean isCommented = line.trim().startsWith("#");
                String content = isCommented ? line.trim().substring(1) : line.trim();
                
                if (content.contains("=")) {
                    String[] keyValue = content.split("=", 2);
                    propertyFile.addLine(new PropertyLine(lineNumber, keyValue[0].trim(), keyValue[1].trim(), isCommented));
                } else {
                    // Ajouter une ligne vide ou autre si nécessaire
                    propertyFile.addLine(new PropertyLine(lineNumber, "", "", isCommented));
                }
                
                lineNumber++;
            }
        }

        return propertyFile;
    }
}
