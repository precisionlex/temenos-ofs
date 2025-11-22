package com.precisionlex;

import java.util.ArrayList;
import java.util.List;

public class OfsEnquiryRequest {

    private String operation = "ENQUIRY.SELECT";
    private String options;  // Empty for enquiries
    private String userId;
    private String password;
    private String company;
    private String enquiryId;
    private List<SelectionCriteria> selectionCriteria;

    public OfsEnquiryRequest() {
        this.selectionCriteria = new ArrayList<>();
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getOptions() {
        return options;
    }

    public void setOptions(String options) {
        this.options = options;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getEnquiryId() {
        return enquiryId;
    }

    public void setEnquiryId(String enquiryId) {
        this.enquiryId = enquiryId;
    }

    public List<SelectionCriteria> getSelectionCriteria() {
        return selectionCriteria;
    }

    public void setSelectionCriteria(List<SelectionCriteria> selectionCriteria) {
        this.selectionCriteria = selectionCriteria;
    }

    public void addSelectionCriteria(String field, String operand, String criteria) {
        this.selectionCriteria.add(new SelectionCriteria(field, operand, criteria));
    }

    public void addSelectionCriteria(SelectionCriteria criteria) {
        this.selectionCriteria.add(criteria);
    }

    public static class SelectionCriteria {
        private String field;
        private String operand;  // EQ, NE, GE, GT, LE, LT, UL, LK, NR
        private String criteria;

        public SelectionCriteria() {}

        public SelectionCriteria(String field, String operand, String criteria) {
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
}

