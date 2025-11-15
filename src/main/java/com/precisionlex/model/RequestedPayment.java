package com.precisionlex.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
}
