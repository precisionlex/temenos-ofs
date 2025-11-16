package com.precisionlex.model;

public class CurrentBlockInfo {

    private String id;
    private String docId;
    private String acLockedId;
    private String accountId;
    private String payerName;
    private String currency;
    private Double amount;
    private String remittanceInformationId;
    private String status;
    private String updatesReported;

    public CurrentBlockInfo() {
    }

    public CurrentBlockInfo(String id,
                            String docId,
                            String acLockedId,
                            String accountId,
                            String payerName,
                            String currency,
                            Double amount,
                            String remittanceInformationId,
                            String status,
                            String updatesReported) {
        this.id = id;
        this.docId = docId;
        this.acLockedId = acLockedId;
        this.accountId = accountId;
        this.payerName = payerName;
        this.currency = currency;
        this.amount = amount;
        this.remittanceInformationId = remittanceInformationId;
        this.status = status;
        this.updatesReported = updatesReported;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getDocId() { return docId; }
    public void setDocId(String docId) { this.docId = docId; }

    public String getAcLockedId() { return acLockedId; }
    public void setAcLockedId(String acLockedId) { this.acLockedId = acLockedId; }

    public String getAccountId() { return accountId; }
    public void setAccountId(String accountId) { this.accountId = accountId; }

    public String getPayerName() { return payerName; }
    public void setPayerName(String payerName) { this.payerName = payerName; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }

    public String getRemittanceInformationId() { return remittanceInformationId; }
    public void setRemittanceInformationId(String remittanceInformationId) { this.remittanceInformationId = remittanceInformationId; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getUpdatesReported() { return updatesReported; }
    public void setUpdatesReported(String updatesReported) { this.updatesReported = updatesReported; }
}
