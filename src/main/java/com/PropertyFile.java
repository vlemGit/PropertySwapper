package com;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class PropertyFile {
    private final Map<Integer, PropertyLine> lines = new LinkedHashMap<>();

    public void addLine(PropertyLine line) {
        lines.put(line.getLineNumber(), line);
    }

    public Collection<PropertyLine> getLines() {
        return new ArrayList<>(lines.values());
    }

    public void updateLine(int lineNumber, String key, String value, boolean isCommented) {
        if (lines.containsKey(lineNumber -1)) {
            lines.put(lineNumber, new PropertyLine(lineNumber, key, value, isCommented));
        }
    }
}