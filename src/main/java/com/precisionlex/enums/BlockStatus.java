package com.precisionlex.enums;

public enum BlockStatus {
    PENDING("Pending"),
    ACTIVE("Active"),
    CANCELED("Canceled"),
    ACCOUNT_CLOSED("AccountClosed");

    private final String label;

    BlockStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
