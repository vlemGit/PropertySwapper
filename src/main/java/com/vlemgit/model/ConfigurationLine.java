package com.vlemgit.model;

import java.util.List;

public class ConfigurationLine {
    private String key;
    private List<String> value;

    public ConfigurationLine(){
        //default contructor for jackson
    }

    public ConfigurationLine(String key, List<String> value){
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public List<String> getValue() {
        return value;
    }

    public void setValue(List<String> value) {
        this.value = value;
    }

}
