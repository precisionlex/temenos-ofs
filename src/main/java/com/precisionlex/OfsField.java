package com.precisionlex;

import java.util.ArrayList;
import java.util.List;

public class OfsField {
    private List<List<String>> data;

    public OfsField() {
        this.data = new ArrayList<>();
    }

    public OfsField(String simpleValue) {
        this();
        setSimpleValue(simpleValue);
    }

    public OfsField(List<String> multiValues) {
        this();
        setMultiValues(multiValues);
    }

    public OfsField(List<List<String>> subValues, boolean isSubValues) {
        this();
        this.data = new ArrayList<>(subValues);
    }

    public void setSimpleValue(String value) {
        this.data.clear();
        List<String> singleValue = new ArrayList<>();
        singleValue.add(value);
        this.data.add(singleValue);
    }

    public String getSimpleValue() {
        if (data.isEmpty() || data.get(0).isEmpty()) {
            return null;
        }
        return data.get(0).get(0);
    }

    public void setMultiValues(List<String> values) {
        this.data.clear();
        for (String value : values) {
            List<String> multiValue = new ArrayList<>();
            multiValue.add(value);
            this.data.add(multiValue);
        }
    }

    public List<String> getMultiValues() {
        List<String> result = new ArrayList<>();
        for (List<String> multiValue : data) {
            if (!multiValue.isEmpty()) {
                result.add(multiValue.get(0));
            }
        }
        return result;
    }

    public void setSubValues(List<List<String>> subValues) {
        this.data = new ArrayList<>(subValues);
    }

    public List<List<String>> getSubValues() {
        return new ArrayList<>(data);
    }

    public boolean isEmpty() {
        return data.isEmpty();
    }

    public int size() {
        return data.size();
    }

    @Override
    public String toString() {
        if (data.isEmpty()) {
            return "";
        }
        if (data.size() == 1 && data.get(0).size() == 1) {
            return data.get(0).get(0);
        }
        return data.toString();
    }
}

