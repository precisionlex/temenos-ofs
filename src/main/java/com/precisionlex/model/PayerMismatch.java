package com.precisionlex.model;

public class PayerMismatch {

    private Long id;
    private String receivedDocId;
    private String iban;
    private String name;
    private String legalId;
    private String status;

    public PayerMismatch() {
    }

    public PayerMismatch(Long id,
                         String receivedDocId,
                         String iban,
                         String name,
                         String legalId,
                         String status) {
        this.id = id;
        this.receivedDocId = receivedDocId;
        this.iban = iban;
        this.name = name;
        this.legalId = legalId;
        this.status = status;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getReceivedDocId() { return receivedDocId; }
    public void setReceivedDocId(String receivedDocId) { this.receivedDocId = receivedDocId; }

    public String getIban() { return iban; }
    public void setIban(String iban) { this.iban = iban; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getLegalId() { return legalId; }
    public void setLegalId(String legalId) { this.legalId = legalId; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
