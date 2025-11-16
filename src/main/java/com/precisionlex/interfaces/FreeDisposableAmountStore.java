package com.precisionlex.interfaces;

import com.precisionlex.model.FreeDisposableAmount;

public interface FreeDisposableAmountStore {
    FreeDisposableAmount findByAccount(String accountId);
    FreeDisposableAmount save(FreeDisposableAmount amount);
}