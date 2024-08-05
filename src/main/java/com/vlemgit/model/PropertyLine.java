package com.vlemgit.model;

public class PropertyLine {
    private int index;
    private String key;
    private String value;
    private boolean isCommented;

    public PropertyLine(int index, String key, String value, boolean isCommented) {
        this.index = index;
        this.key = key;
        this.value = value;
        this.isCommented = isCommented;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
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