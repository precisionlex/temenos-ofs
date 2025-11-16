package com.precisionlex.interfaces;

import com.precisionlex.model.AvailableAccountBalance;

public interface AvailableBalanceStore {
    AvailableAccountBalance findByAccountId(String accountId);
    AvailableAccountBalance save(AvailableAccountBalance balance);
}