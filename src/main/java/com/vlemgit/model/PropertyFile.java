package com.vlemgit.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class PropertyFile {
    private final Map<Integer, PropertyLine> lines = new LinkedHashMap<>();

    public void addLine(PropertyLine line) {
        lines.put(line.getIndex(), line);
    }

    public Collection<PropertyLine> getLines() {
        return new ArrayList<>(lines.values());
    }

    public void updateLine(int index, String key, String value, boolean isCommented) {
        if (lines.containsKey(index -1)) {
            lines.put(index, new PropertyLine(index, key, value, isCommented));
        }
    }
}