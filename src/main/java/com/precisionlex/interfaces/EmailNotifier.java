package com.precisionlex.interfaces;

import com.precisionlex.model.RequestedPayment;
import com.precisionlex.model.PayerMismatch;

public interface EmailNotifier {
    void notifyPaymentInit(RequestedPayment payment);
    void notification(PayerMismatch mismatch);
    void notification(String type, String customerId, String docId, String acLockedId);
}
