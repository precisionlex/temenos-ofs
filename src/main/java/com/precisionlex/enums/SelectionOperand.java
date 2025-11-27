package com.precisionlex.enums;


public enum SelectionOperand {

    EQ("EQ"),

    LK("LK"),

    NE("NE"),

    UL("UL"),

    GT("GT"),

    GE("GE"),

    LT("LT"),

    LE("LE"),

    RG("RG");

    private final String code;

    SelectionOperand(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static SelectionOperand fromCode(String code) {
        if (code == null) {
            throw new IllegalArgumentException("Selection operand code cannot be null");
        }

        String upperCode = code.trim().toUpperCase();
        for (SelectionOperand operand : values()) {
            if (operand.code.equals(upperCode)) {
                return operand;
            }
        }

        throw new IllegalArgumentException("Invalid selection operand code: " + code +
            ". Valid operands are: EQ, LK, NE, UL, GT, GE, LT, LE, RG");
    }

    @Override
    public String toString() {
        return code;
    }
}

