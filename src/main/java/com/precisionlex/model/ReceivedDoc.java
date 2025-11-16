package com.precisionlex.model;

public class ReceivedDoc {

    private String id;
    private String payerAccount;
    private String payerName;
    private String payerCode;
    private String payerType;
    private String currency;
    private Double amount;

    private String remittanceInformationId;
    private String remittanceInformationReason;
    private String returnResult;
    private String rejectReason;
    private String type;
    private String businessArea;
    private String executeType;
    private Double freeDisposableAmount;

    private String receivedMessageId;
    private String currentBlockInfoId;

    private double nameJaroWinklerScore;
    private double nameLevenshteinScore;

    public ReceivedDoc() {
    }

    public ReceivedDoc(String id,
                       String payerAccount,
                       String payerName,
                       String payerCode,
                       String payerType,
                       String currency,
                       Double amount,
                       String remittanceInformationId,
                       String remittanceInformationReason,
                       String returnResult,
                       String rejectReason,
                       String type,
                       String businessArea,
                       String executeType,
                       Double freeDisposableAmount,
                       String receivedMessageId,
                       String currentBlockInfoId,
                       double nameJaroWinklerScore,
                       double nameLevenshteinScore) {
        this.id = id;
        this.payerAccount = payerAccount;
        this.payerName = payerName;
        this.payerCode = payerCode;
        this.payerType = payerType;
        this.currency = currency;
        this.amount = amount;
        this.remittanceInformationId = remittanceInformationId;
        this.remittanceInformationReason = remittanceInformationReason;
        this.returnResult = returnResult;
        this.rejectReason = rejectReason;
        this.type = type;
        this.businessArea = businessArea;
        this.executeType = executeType;
        this.freeDisposableAmount = freeDisposableAmount;
        this.receivedMessageId = receivedMessageId;
        this.currentBlockInfoId = currentBlockInfoId;
        this.nameJaroWinklerScore = nameJaroWinklerScore;
        this.nameLevenshteinScore = nameLevenshteinScore;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    // backward compatibility
    public String getDocId() { return id; }
    public void setDocId(String docId) { this.id = docId; }

    public String getPayerAccount() { return payerAccount; }
    public void setPayerAccount(String payerAccount) { this.payerAccount = payerAccount; }

    public String getPayerName() { return payerName; }
    public void setPayerName(String payerName) { this.payerName = payerName; }

    public String getPayerCode() { return payerCode; }
    public void setPayerCode(String payerCode) { this.payerCode = payerCode; }

    public String getPayerType() { return payerType; }
    public void setPayerType(String payerType) { this.payerType = payerType; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }

    public String getRemittanceInformationId() { return remittanceInformationId; }
    public void setRemittanceInformationId(String remittanceInformationId) { this.remittanceInformationId = remittanceInformationId; }

    public String getRemittanceInformationReason() { return remittanceInformationReason; }
    public void setRemittanceInformationReason(String remittanceInformationReason) { this.remittanceInformationReason = remittanceInformationReason; }

    public String getReturnResult() { return returnResult; }
    public void setReturnResult(String returnResult) { this.returnResult = returnResult; }

    public String getRejectReason() { return rejectReason; }
    public void setRejectReason(String rejectReason) { this.rejectReason = rejectReason; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getBusinessArea() { return businessArea; }
    public void setBusinessArea(String businessArea) { this.businessArea = businessArea; }

    public String getExecuteType() { return executeType; }
    public void setExecuteType(String executeType) { this.executeType = executeType; }

    public Double getFreeDisposableAmount() { return freeDisposableAmount; }
    public void setFreeDisposableAmount(Double freeDisposableAmount) { this.freeDisposableAmount = freeDisposableAmount; }

    public String getReceivedMessageId() { return receivedMessageId; }
    public void setReceivedMessageId(String receivedMessageId) { this.receivedMessageId = receivedMessageId; }

    public String getCurrentBlockInfoId() { return currentBlockInfoId; }
    public void setCurrentBlockInfoId(String currentBlockInfoId) { this.currentBlockInfoId = currentBlockInfoId; }

    public double getNameJaroWinklerScore() { return nameJaroWinklerScore; }
    public void setNameJaroWinklerScore(double nameJaroWinklerScore) { this.nameJaroWinklerScore = nameJaroWinklerScore; }

    public double getNameLevenshteinScore() { return nameLevenshteinScore; }
    public void setNameLevenshteinScore(double nameLevenshteinScore) { this.nameLevenshteinScore = nameLevenshteinScore; }
}
