package com.precisionlex.enums;

public enum ReturnResult {
    ACCEPTED("T"),
    DECLINED("F");

    private final String label;

    ReturnResult(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
