package com.precisionlex.interfaces;

public interface RequestedPaymentStore {
    RequestedPayment findByInstrId(String instrId);
    RequestedPayment save(RequestedPayment payment);
}