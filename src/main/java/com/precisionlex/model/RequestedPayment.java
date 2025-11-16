package com.precisionlex.model;

public class RequestedPayment {

    private String paymentId;
    private String executionDate;
    private String dbtrName;
    private String dbtrAccount;
    private String dbtrBic;
    private String chargeBearer;
    private String paymentType;
    private String cdtrName;
    private String cdtrAccount;
    private String cdtrBic;
    private double amount;
    private String currency;
    private String pmtDetailsUstrd;

    private String instrId;
    private String endToEndId;
    private String returnResult;
    private String processStatus;
    private String ftNumber;
    private double sentAmount;

    private String currentBlockInfoId;
    private String receivedMessageId;
    private String paymentGroupHeaderId;

    public RequestedPayment() {
    }

    public RequestedPayment(String paymentId,
                            String executionDate,
                            String dbtrName,
                            String dbtrAccount,
                            String dbtrBic,
                            String chargeBearer,
                            String paymentType,
                            String cdtrName,
                            String cdtrAccount,
                            String cdtrBic,
                            double amount,
                            String currency,
                            String pmtDetailsUstrd,
                            String instrId,
                            String endToEndId,
                            String returnResult,
                            String processStatus,
                            String ftNumber,
                            double sentAmount,
                            String currentBlockInfoId,
                            String receivedMessageId,
                            String paymentGroupHeaderId) {
        this.paymentId = paymentId;
        this.executionDate = executionDate;
        this.dbtrName = dbtrName;
        this.dbtrAccount = dbtrAccount;
        this.dbtrBic = dbtrBic;
        this.chargeBearer = chargeBearer;
        this.paymentType = paymentType;
        this.cdtrName = cdtrName;
        this.cdtrAccount = cdtrAccount;
        this.cdtrBic = cdtrBic;
        this.amount = amount;
        this.currency = currency;
        this.pmtDetailsUstrd = pmtDetailsUstrd;
        this.instrId = instrId;
        this.endToEndId = endToEndId;
        this.returnResult = returnResult;
        this.processStatus = processStatus;
        this.ftNumber = ftNumber;
        this.sentAmount = sentAmount;
        this.currentBlockInfoId = currentBlockInfoId;
        this.receivedMessageId = receivedMessageId;
        this.paymentGroupHeaderId = paymentGroupHeaderId;
    }

    public String getPaymentId() { return paymentId; }
    public void setPaymentId(String paymentId) { this.paymentId = paymentId; }

    public String getExecutionDate() { return executionDate; }
    public void setExecutionDate(String executionDate) { this.executionDate = executionDate; }

    public String getDbtrName() { return dbtrName; }
    public void setDbtrName(String dbtrName) { this.dbtrName = dbtrName; }

    public String getDbtrAccount() { return dbtrAccount; }
    public void setDbtrAccount(String dbtrAccount) { this.dbtrAccount = dbtrAccount; }

    public String getDbtrBic() { return dbtrBic; }
    public void setDbtrBic(String dbtrBic) { this.dbtrBic = dbtrBic; }

    public String getChargeBearer() { return chargeBearer; }
    public void setChargeBearer(String chargeBearer) { this.chargeBearer = chargeBearer; }

    public String getPaymentType() { return paymentType; }
    public void setPaymentType(String paymentType) { this.paymentType = paymentType; }

    public String getCdtrName() { return cdtrName; }
    public void setCdtrName(String cdtrName) { this.cdtrName = cdtrName; }

    public String getCdtrAccount() { return cdtrAccount; }
    public void setCdtrAccount(String cdtrAccount) { this.cdtrAccount = cdtrAccount; }

    public String getCdtrBic() { return cdtrBic; }
    public void setCdtrBic(String cdtrBic) { this.cdtrBic = cdtrBic; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public String getPmtDetailsUstrd() { return pmtDetailsUstrd; }
    public void setPmtDetailsUstrd(String pmtDetailsUstrd) { this.pmtDetailsUstrd = pmtDetailsUstrd; }

    public String getInstrId() { return instrId; }
    public void setInstrId(String instrId) { this.instrId = instrId; }

    public String getEndToEndId() { return endToEndId; }
    public void setEndToEndId(String endToEndId) { this.endToEndId = endToEndId; }

    public String getReturnResult() { return returnResult; }
    public void setReturnResult(String returnResult) { this.returnResult = returnResult; }

    public String getProcessStatus() { return processStatus; }
    public void setProcessStatus(String processStatus) { this.processStatus = processStatus; }

    public String getFtNumber() { return ftNumber; }
    public void setFtNumber(String ftNumber) { this.ftNumber = ftNumber; }

    public double getSentAmount() { return sentAmount; }
    public void setSentAmount(double sentAmount) { this.sentAmount = sentAmount; }

    public String getCurrentBlockInfoId() { return currentBlockInfoId; }
    public void setCurrentBlockInfoId(String currentBlockInfoId) { this.currentBlockInfoId = currentBlockInfoId; }

    public String getReceivedMessageId() { return receivedMessageId; }
    public void setReceivedMessageId(String receivedMessageId) { this.receivedMessageId = receivedMessageId; }

    public String getPaymentGroupHeaderId() { return paymentGroupHeaderId; }
    public void setPaymentGroupHeaderId(String paymentGroupHeaderId) { this.paymentGroupHeaderId = paymentGroupHeaderId; }
}
