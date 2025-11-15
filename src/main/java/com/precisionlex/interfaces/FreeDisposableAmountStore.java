package com.precisionlex.interfaces;

public interface FreeDisposableAmountStore {
    FreeDisposableAmount findByAccount(String accountId);
    FreeDisposableAmount save(FreeDisposableAmount amount);
}