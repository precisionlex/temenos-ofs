package com.precisionlex.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReceivedDoc {

    private String docId;
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

    // Instead of @ManyToOne relations, just store IDs or references
    private String receivedMessageId;
    private String currentBlockInfoId;

    private double nameJaroWinklerScore;
    private double nameLevenshteinScore;
}
