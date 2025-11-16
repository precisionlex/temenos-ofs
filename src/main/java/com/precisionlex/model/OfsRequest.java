package com.precisionlex.model;

public class OfsRequest {

    private String id;
    private String correlationId;
    private String requestType;
    private String ofsRequestText;
    private String ofsResponseText;
    private String documentId;
    private String receivedMessageId;
    private String currentBlockInfoId;

    public OfsRequest() {
    }

    public OfsRequest(String id,
                      String correlationId,
                      String requestType,
                      String ofsRequestText,
                      String ofsResponseText,
                      String documentId,
                      String receivedMessageId,
                      String currentBlockInfoId) {
        this.id = id;
        this.correlationId = correlationId;
        this.requestType = requestType;
        this.ofsRequestText = ofsRequestText;
        this.ofsResponseText = ofsResponseText;
        this.documentId = documentId;
        this.receivedMessageId = receivedMessageId;
        this.currentBlockInfoId = currentBlockInfoId;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getCorrelationId() {
        return correlationId;
    }
    public void setCorrelationId(String correlationId) {
        this.correlationId = correlationId;
    }

    public String getRequestType() {
        return requestType;
    }
    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public String getOfsRequestText() {
        return ofsRequestText;
    }
    public void setOfsRequestText(String ofsRequestText) {
        this.ofsRequestText = ofsRequestText;
    }

    public String getOfsResponseText() {
        return ofsResponseText;
    }
    public void setOfsResponseText(String ofsResponseText) {
        this.ofsResponseText = ofsResponseText;
    }

    public String getDocumentId() {
        return documentId;
    }
    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getReceivedMessageId() {
        return receivedMessageId;
    }
    public void setReceivedMessageId(String receivedMessageId) {
        this.receivedMessageId = receivedMessageId;
    }

    public String getCurrentBlockInfoId() {
        return currentBlockInfoId;
    }
    public void setCurrentBlockInfoId(String currentBlockInfoId) {
        this.currentBlockInfoId = currentBlockInfoId;
    }
}
