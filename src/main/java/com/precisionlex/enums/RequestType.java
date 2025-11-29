package com.precisionlex.enums;

public enum RequestType {

    TRANSACTION("Transaction"),

    ENQUIRY("Enquiry");

    private final String value;

    RequestType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }
}

