package com.precisionlex;

import com.precisionlex.enums.SelectionOperand;

public class OfsEnquirySelectionCriteria {
    private String field;
    private SelectionOperand operand;
    private String criteria;

    public OfsEnquirySelectionCriteria() {}

    public OfsEnquirySelectionCriteria(String field, SelectionOperand operand, String criteria) {
        this.field = field;
        this.operand = operand;
        this.criteria = criteria;
    }

    public OfsEnquirySelectionCriteria(String field, String operand, String criteria) {
        this.field = field;
        this.operand = SelectionOperand.fromCode(operand);
        this.criteria = criteria;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public SelectionOperand getOperand() { return operand; }

    public void setOperand(SelectionOperand operand) { this.operand = operand; }

    public void setOperand(String operand) {this.operand = SelectionOperand.fromCode(operand);}

    public String getCriteria() {
        return criteria;
    }

    public void setCriteria(String criteria) {
        this.criteria = criteria;
    }

    @Override
    public String toString() {return field + ":" + operand.getCode() + "=" + criteria;}
}

