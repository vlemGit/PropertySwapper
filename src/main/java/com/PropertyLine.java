package com;

public class PropertyLine {
    private int lineNumber;
    private String key;
    private String value;
    private boolean isCommented;

    public PropertyLine(int lineNumber, String key, String value, boolean isCommented) {
        this.lineNumber = lineNumber;
        this.key = key;
        this.value = value;
        this.isCommented = isCommented;
    }

    // Getters and Setters
    public int getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isCommented() {
        return isCommented;
    }

    public void setCommented(boolean isCommented) {
        this.isCommented = isCommented;
    }

    @Override
    public String toString() {
        if("".equals(this.key)){
            return key + value;
        }
        return key + "=" + value;
    }
}