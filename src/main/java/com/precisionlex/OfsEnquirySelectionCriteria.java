package com.precisionlex;

public class OfsEnquirySelectionCriteria {
    private String field;
    private String operand;  // EQ, NE, GE, GT, LE, LT, UL, LK, NR
    private String criteria;

    public OfsEnquirySelectionCriteria() {}

    public OfsEnquirySelectionCriteria(String field, String operand, String criteria) {
        this.field = field;
        this.operand = operand;
        this.criteria = criteria;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getOperand() {
        return operand;
    }

    public void setOperand(String operand) {
        this.operand = operand;
    }

    public String getCriteria() {
        return criteria;
    }

    public void setCriteria(String criteria) {
        this.criteria = criteria;
    }

    @Override
    public String toString() {
        return field + ":" + operand + "=" + criteria;
    }
}

