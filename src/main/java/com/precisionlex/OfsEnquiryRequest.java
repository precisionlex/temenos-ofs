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
    private List<OfsEnquirySelectionCriteria> selectionCriteria;

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

    public List<OfsEnquirySelectionCriteria> getSelectionCriteria() {
        return selectionCriteria;
    }

    public void setSelectionCriteria(List<OfsEnquirySelectionCriteria> selectionCriteria) {
        this.selectionCriteria = selectionCriteria;
    }

    public void addSelectionCriteria(String field, String operand, String criteria) {
        this.selectionCriteria.add(new OfsEnquirySelectionCriteria(field, operand, criteria));
    }

    public void addSelectionCriteria(OfsEnquirySelectionCriteria criteria) {
        this.selectionCriteria.add(criteria);
    }
}

