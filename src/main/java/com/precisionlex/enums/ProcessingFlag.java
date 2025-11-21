package com.precisionlex.enums;

public enum ProcessingFlag {

    PROCESS("PROCESS"),

    VALIDATE("VALIDATE");

    private final String value;

    ProcessingFlag(String value) {
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

