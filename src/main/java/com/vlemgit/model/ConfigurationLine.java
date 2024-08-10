package com.vlemgit.model;

public class ConfigurationLine {
    private int line;
    private String key;
    private String value;

    public ConfigurationLine(int line, String key, String value){
        this.line = line;
        this.key = key;
        this.value = value;
    } 

    public int getLine() {
        return line;
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
    
}
