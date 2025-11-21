package com.precisionlex;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class OfsResponse {

    private String recordId;

    private String transactionRef;

    private String status;

    private String errorCode;

    private Map<String, List<OfsField>> fields;

    public OfsResponse() {
        this.fields = new LinkedHashMap<>();
    }

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public String getTransactionRef() {
        return transactionRef;
    }

    public void setTransactionRef(String transactionRef) {
        this.transactionRef = transactionRef;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public Map<String, List<OfsField>> getFields() {
        return fields;
    }

    public void setFields(Map<String, List<OfsField>> fields) {
        this.fields = fields;
    }

    public boolean isSuccess() {
        if (status == null) {
            return false;
        }
        try {
            return Integer.parseInt(status) > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public boolean isError() {
        if (status == null) {
            return false;
        }
        try {
            return Integer.parseInt(status) < 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public String getFieldValue(String fieldName) {
        List<OfsField> fieldList = fields.get(fieldName);
        if (fieldList == null || fieldList.isEmpty()) {
            return null;
        }
        return fieldList.get(0).getSimpleValue();
    }

    @Override
    public String toString() {
        return "OfsResponse{" +
                "recordId='" + recordId + '\'' +
                ", transactionRef='" + transactionRef + '\'' +
                ", status='" + status + '\'' +
                ", errorCode='" + errorCode + '\'' +
                ", isSuccess=" + isSuccess() +
                ", fieldCount=" + fields.size() +
                '}';
    }
}

