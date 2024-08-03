package com.vlemgit;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Property {
    private final StringProperty key;
    private final StringProperty value;

    public Property(String key, String value) {
        this.key = new SimpleStringProperty(key);
        this.value = new SimpleStringProperty(value);
    }

    public String getKey() {
        return key.get();
    }

    public StringProperty keyProperty() {
        return key;
    }

    public String getValue() {
        return value.get();
    }

    public StringProperty valueProperty() {
        return value;
    }
}
