package com.precisionlex;

import com.precisionlex.enums.Function;
import com.precisionlex.enums.ProcessingFlag;
import com.precisionlex.enums.RequestType;

import java.util.List;
import java.util.Map;

public class OfsTransactionRequest {

    private String application;
    private String version;
    private String requestType;
    private String function;
    private String processingFlag;
    private String options;
    private String userId;
    private String password;
    private String company;
    private String recordId;
    private Map<String, List<OfsField>> fields;

    public OfsTransactionRequest() { this.fields = new java.util.LinkedHashMap<>(); }

    public String getApplication() {
        return application;
    }

    public void setApplication(String application) {
        this.application = application;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public void setRequestType(RequestType requestType) {
        this.requestType = requestType.getValue();
    }

    public String getFunction() {
        return function;
    }

    public void setFunction(String function) {
        this.function = function;
    }

    public void setFunction(Function function) {
        this.function = function.getValue();
    }

    public String getProcessingFlag() {
        return processingFlag;
    }

    public void setProcessingFlag(String processingFlag) {
        this.processingFlag = processingFlag;
    }

    public void setProcessingFlag(ProcessingFlag processingFlag) {
        this.processingFlag = processingFlag.getValue();
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

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public Map<String, List<OfsField>> getFields() {
        return fields;
    }

    public void setFields(Map<String, List<OfsField>> fields) {
        this.fields = fields;
    }

    public void setField(String key, OfsField field) {
        this.fields.put(key, List.of(field));
    }

    public void setField(String key, String singleValue) {
        OfsField field = new OfsField(singleValue);
        this.fields.put(key, List.of(field));
    }

    public void setField(String key, List<String> multiValues) {
        OfsField field = new OfsField(multiValues);
        this.fields.put(key, List.of(field));
    }
}
