package com.precisionlex.interfaces;

import com.precisionlex.model.RequestedPayment;

public interface RequestedPaymentStore {
    RequestedPayment findByInstrId(String instrId);
    RequestedPayment save(RequestedPayment payment);
}