package com.precisionlex.interfaces;

import com.precisionlex.model.PayerMismatch;

public interface PayerMismatchStore {
    PayerMismatch save(PayerMismatch mismatch);
}