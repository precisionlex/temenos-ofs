package com.precisionlex.interfaces;

import com.precisionlex.model.CurrentBlockInfo;
import java.util.List;

public interface BlockInfoStore {
    CurrentBlockInfo findById(String id);
    CurrentBlockInfo save(CurrentBlockInfo blockInfo);
    List<CurrentBlockInfo> findByAccountId(String accountId);
    void saveAll(List<CurrentBlockInfo> blocks);

    List<CurrentBlockInfo> findActiveByAccountId(String accountId);
}
