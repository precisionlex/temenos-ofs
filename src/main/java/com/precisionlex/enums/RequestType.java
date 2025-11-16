package com.precisionlex.enums;

import java.util.Arrays;

public enum RequestType {
    NEW_BLOCK("NewBlock"),
    PAYER_VALIDATE("PayerValidate"),
    PAYER_VALIDATE_NS("PayerValidateNS"),
    PAYER_VALIDATE_CARDCHN("PayerValidateCardChn"),
    PAYER_VALIDATE_CARDCAN("PayerValidateCardCan"),
    GET_BALANCE("GetBalance"),
    CARD_CAN("CardCan"),
    CARD_STOP("CardStop"),
    CARD_CHN("CardChn"),
    PAYM_STATUS("PaymStatus"),
    PAYM_REVERSE_LOCK("PaymLockReverse"),
    PAYM_INIT("PaymInit");

    private final String label;

    RequestType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public static RequestType fromLabel(String label) {
        return Arrays.stream(values())
                .filter(x -> x.getLabel().equalsIgnoreCase(label))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No enum constant with label " + label));
    }
}
