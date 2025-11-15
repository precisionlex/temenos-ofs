package com.precisionlex.interfaces;

public interface BlockInfoStore {
    List<CurrentBlockInfo> findActiveByAccountId(String accountId);
}