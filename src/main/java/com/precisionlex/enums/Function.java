package com.precisionlex.enums;

public enum Function {

    INPUT("I"),

    AUTHORISE("A"),

    DELETE("D"),

    REVERSE("R");

    private final String value;

    Function(String value) {
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

