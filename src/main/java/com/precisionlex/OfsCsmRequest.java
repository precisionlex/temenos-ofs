package com.precisionlex;

import java.util.ArrayList;
import java.util.List;

public class OfsCsmRequest {
    private String interfaceType = "CSM";
    private String operation;
    private String options;
    private String userId;
    private String password;
    private String company;
    private List<String> extendedUserInformation = new ArrayList<>();
    private String processingRule;
    private List<String> entries = new ArrayList<>();

    public void setInterfaceType(String interfaceType) {
        this.interfaceType = interfaceType;
    }

    public String getInterfaceType() {
        return interfaceType;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getOperation() {
        return operation;
    }

    public String getOptions() {
        return options;
    }

    public void setOptions(String options) {
        this.options = options;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }       

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getCompany() {
        return company;
    }

    public List<String> getExtendedUserInformation() {
        return extendedUserInformation;
    }

    public void setExtendedUserInformation(List<String> extendedUserInformation) {
        this.extendedUserInformation = extendedUserInformation;
    }

    public String getProcessingRule() {
        return processingRule;
    }

    public void setProcessingRule(String processingRule) {
        this.processingRule = processingRule;
    }

    public List<String> getEntries() {
        return entries;
    }

    public void setEntries(List<String> entries) {
        this.entries = entries;
    }

    public void addEntry(String entry) {
        entries.add(entry);
    }
}

