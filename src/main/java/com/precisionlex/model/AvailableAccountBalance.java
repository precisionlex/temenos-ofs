package com.precisionlex.model;

public class AvailableAccountBalance {

    private Long id;
    private String accountId;
    private double availableAmt;

    public AvailableAccountBalance() {
    }

    public AvailableAccountBalance(Long id, String accountId, double availableAmt) {
        this.id = id;
        this.accountId = accountId;
        this.availableAmt = availableAmt;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getAccountId() { return accountId; }
    public void setAccountId(String accountId) { this.accountId = accountId; }

    public double getAvailableAmt() { return availableAmt; }
    public void setAvailableAmt(double availableAmt) { this.availableAmt = availableAmt; }
}
