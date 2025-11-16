package com.precisionlex.model;

public class FreeDisposableAmount {

    private Long id;
    private String payerAccount;
    private double amount;
    private String receivedDocId;

    public FreeDisposableAmount() {
    }

    public FreeDisposableAmount(Long id,
                                String payerAccount,
                                double amount,
                                String receivedDocId) {
        this.id = id;
        this.payerAccount = payerAccount;
        this.amount = amount;
        this.receivedDocId = receivedDocId;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getPayerAccount() { return payerAccount; }
    public void setPayerAccount(String payerAccount) { this.payerAccount = payerAccount; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public String getReceivedDocId() { return receivedDocId; }
    public void setReceivedDocId(String receivedDocId) { this.receivedDocId = receivedDocId; }
}
